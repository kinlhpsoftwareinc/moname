package com.kinlhp.moname.commons.test.spring.security;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.net.ssl.SSLContext;
import java.lang.reflect.Constructor;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.kinlhp.moname.commons.test.spring.security.ClientCredentialsFlow.Token;
import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.KeycloakAutoConfiguration;
import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.PropertiesKeycloakConnectionDetails;

import static jakarta.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static jakarta.ws.rs.core.Response.Status.OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.function.Predicate.not;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Resolves {@link MockMvc MockMvc} parameters of test class constructors, test methods and lifecycle methods (e.g.
 * {@link org.junit.jupiter.api.BeforeEach @BeforeEach}) with a {@link MockMvc MockMvc} whose requests carry the
 * {@code Authorization} header with the access token of a {@link ClientCredentialsFlow @ClientCredentialsFlow} client,
 * taken from, in order of precedence:
 * <ol>
 *     <li>the annotation on the parameter;</li>
 *     <li>
 *         the annotation on the test method (not on the lifecycle method, e.g.
 *         {@link org.junit.jupiter.api.BeforeEach @BeforeEach}), for method parameters only, unless the parameter is
 *         autowirable ({@link org.springframework.beans.factory.annotation.Autowired @Autowired},
 *         {@link org.springframework.beans.factory.annotation.Qualifier @Qualifier} or
 *         {@link org.springframework.beans.factory.annotation.Value @Value}), in which case it is left to Spring.
 *     </li>
 * </ol>
 * An annotated parameter is ambiguous when it is autowirable or belongs to an autowirable constructor, since
 * {@link SpringExtension SpringExtension} supports it as well, so JUnit fails with a
 * {@link ParameterResolutionException ParameterResolutionException}.
 * <p>
 * A {@link MockMvc MockMvc} resolved in a constructor lives as long as the test instance, so with
 * {@link org.junit.jupiter.api.TestInstance.Lifecycle#PER_CLASS PER_CLASS} its token is not renewed once expired.
 * <p>
 * When no access token can be obtained, the header is omitted. Any other {@link MockMvc MockMvc}, such as an injected
 * field, is left untouched.
 */
public class ClientCredentialsFlowExtension implements ParameterResolver {

	private static final Logger LOG = LoggerFactory.getLogger(ClientCredentialsFlowExtension.class);

	private static final Map<ClientCredentialsFlow, AccessTokenHolder> CACHE = new ConcurrentHashMap<>();

	/**
	 * Safe private lock-splitting approach.
	 */
	private static final Object MOCK_MVC_BUILDER_PRIVATE_LOCK = new MockMvcBuilderPrivateLock();

	@Override
	public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) {
		return isMockMvcParameter(parameterContext)
				&& clientCredentialsFlowOf(parameterContext, extensionContext).isPresent();
	}

	@Override
	public MockMvc resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) {
		final var clientCredentials = clientCredentialsFlowOf(parameterContext, extensionContext).orElseThrow(() ->
				new ParameterResolutionException("There is no @ClientCredentialsFlow for the parameter '%s'"
						.formatted(parameterContext.getParameter()))
		);
		return createAuthorizedMockMvc(extensionContext, accessTokenOf(extensionContext, clientCredentials));
	}

	private boolean isMockMvcParameter(final ParameterContext parameterContext) {
		return MockMvc.class.equals(parameterContext.getParameter().getType());
	}

	private Optional<ClientCredentialsFlow> clientCredentialsFlowOf(final ParameterContext parameterContext,
			final ExtensionContext extensionContext) {
		return parameterContext.findAnnotation(ClientCredentialsFlow.class)
				.or(() -> isConstructorParameter(parameterContext) || isAutowirable(parameterContext)
						? Optional.empty()
						: testMethodClientCredentialsFlowOf(extensionContext));
	}

	private boolean isConstructorParameter(final ParameterContext parameterContext) {
		return parameterContext.getDeclaringExecutable() instanceof Constructor<?>;
	}

	private boolean isAutowirable(final ParameterContext parameterContext) {
		return ParameterResolutionDelegate.isAutowirable(parameterContext.getParameter(), parameterContext.getIndex());
	}

	private Optional<ClientCredentialsFlow> testMethodClientCredentialsFlowOf(final ExtensionContext context) {
		return context.getTestMethod()
				.flatMap(testMethod -> AnnotationSupport.findAnnotation(testMethod, ClientCredentialsFlow.class));
	}

	private Token accessTokenOf(final ExtensionContext context, final ClientCredentialsFlow clientCredentials) {
		return CACHE.computeIfAbsent(clientCredentials, AccessTokenHolder::new)
				.validOrRenew(accessTokenRenewal(context, clientCredentials));
	}

	private Predicate<ExtensionContext> canRequestAccessToken() {
		return runningKeycloak().and(hasKeycloakConnectionDetails());
	}

	private Predicate<ExtensionContext> runningKeycloak() {
		// TODO: Log
		return context -> SpringExtension.getApplicationContext(context)
				.containsBeanDefinition(ExtendableKeycloakContainer.class.getName());
	}

	private Predicate<ExtensionContext> hasKeycloakConnectionDetails() {
		// TODO: Log
		return context -> SpringExtension.getApplicationContext(context)
				.containsBeanDefinition(KeycloakAutoConfiguration.class.getName());
	}

	private Supplier<Optional<Token>> accessTokenRenewal(final ExtensionContext context,
			final ClientCredentialsFlow clientCredentials) {
		return () -> canRequestAccessToken().test(context)
				? Optional.of(requestAccessToken(context, clientCredentials))
				: Optional.empty();
	}

	private Token requestAccessToken(final ExtensionContext context, final ClientCredentialsFlow clientCredentials) {
		return sendAccessTokenRequest(context, propertiesKeycloakConnectionDetailsOf(context), clientCredentials);
	}

	private PropertiesKeycloakConnectionDetails propertiesKeycloakConnectionDetailsOf(final ExtensionContext context) {
		return SpringExtension.getApplicationContext(context).getBean(PropertiesKeycloakConnectionDetails.class);
	}

	private Token sendAccessTokenRequest(final ExtensionContext context,
			final PropertiesKeycloakConnectionDetails keycloakConnectionDetails,
			final ClientCredentialsFlow clientCredentials) {
		try (final var httpClient = httpClientBuilder(context).build()) {
			final var httpRequest = buildHttpRequest(keycloakConnectionDetails, clientCredentials);
			LOG.trace("Requesting a new access token for client '{}' pointing to {}",
					clientCredentials.clientId(), keycloakConnectionDetails.getTokenUri());
			final var httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());
			return extractAccessToken(httpResponse, clientCredentials);
		} catch (final Exception exception) {
			LOG.error("// TODO: Log", exception); // TODO: Log
			throw new RuntimeException("// TODO: Message", exception); // TODO: Message
		}
	}

	private HttpClient.Builder httpClientBuilder(final ExtensionContext context) {
		return tlsHttpClientBuilder(sslContextOf(context));
	}

	private HttpClient.Builder tlsHttpClientBuilder(final SSLContext sslContext) {
		// TODO: Log
		return HttpClient.newBuilder().sslContext(sslContext);
	}

	private SSLContext sslContextOf(final ExtensionContext context) {
		return SslBundleBackedBySsl.of(
				objectProviderOf(context, ServerProperties.class),
				objectProviderOf(context, SslBundles.class)
		).createSslContext();
	}

	private <T> ObjectProvider<T> objectProviderOf(final ExtensionContext context, final Class<T> requiredType) {
		// TODO: log
		return SpringExtension.getApplicationContext(context).getBeanProvider(requiredType);
	}

	private HttpRequest buildHttpRequest(final PropertiesKeycloakConnectionDetails keycloakConnectionDetails,
			final ClientCredentialsFlow clientCredentials) {
		return HttpRequest.newBuilder(keycloakConnectionDetails.getTokenUri())
				.header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED)
				.POST(BodyPublishers.ofString(createXWwwFormUrlencoded(clientCredentials)))
				.build();
	}

	private String createXWwwFormUrlencoded(final ClientCredentialsFlow clientCredentialsFlow) {
		return Map.of(
						"client_id", clientCredentialsFlow.clientId(),
						"client_secret", clientCredentialsFlow.clientSecret(),
						"grant_type", clientCredentialsFlow.grantType()
				)
				.entrySet().stream()
				.map(param -> String.join("=", param.getKey(), URLEncoder.encode(param.getValue(), UTF_8)))
				.collect(Collectors.joining("&"));
	}

	private Token extractAccessToken(final HttpResponse<String> httpResponse,
			final ClientCredentialsFlow clientCredentials) {
		// TODO: To be refactored in favor of use some design pattern (maybe strategy pattern)
		return httpResponse.statusCode() == OK.getStatusCode()
				? doExtractAccessToken(httpResponse)
				: createEmptyAccessToken(httpResponse, clientCredentials);
	}

	private Token doExtractAccessToken(final HttpResponse<String> httpResponse) {
		try {
			final var accessToken = new ObjectMapper().readValue(httpResponse.body(), Token.class);
			LOG.debug(
					"{} token successfully obtained and expires at {}",
					accessToken.tokenType(),
					accessToken.expiresIn()
			);
			return accessToken;
		} catch (final JacksonException exception) {
			LOG.error("// TODO: Log", exception); // TODO: Log
			throw new RuntimeException("// TODO: Message", exception); // TODO: Message
		}
	}

	@SuppressWarnings("java:S2629")
	private Token createEmptyAccessToken(final HttpResponse<String> httpResponse,
			final ClientCredentialsFlow clientCredentials) {
		LOG.warn("Status code {}: {}", httpResponse.statusCode(), httpResponse.body());
		LOG.warn(
				"""
						An empty access token will be defined since a new access token can not be obtained for client \
						'{}' pointing to {}""",
				clientCredentials.clientId(),
				httpResponse.request().uri()
		);
		return Token.empty();
	}

	/**
	 * When {@link MockMvc MockMvc} is autoconfigured
	 * ({@link org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc @AutoConfigureMockMvc},
	 * {@link org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest @WebMvcTest}, ...), the
	 * {@link MockMvc MockMvc} is built by the very {@link MockMvcBuilder MockMvcBuilder} bean that built the
	 * {@link MockMvc MockMvc} bean, so it keeps every customization already applied to it: filters (the Spring
	 * Security filter chain included), print settings (print only on failure included), the Spring Security test
	 * context and any custom one.
	 * <p>
	 * The customizations are not applied again to a new builder because some of them can only be applied once per
	 * application context, e.g. print only on failure registers a singleton bean. As the
	 * {@link MockMvcBuilder MockMvcBuilder} bean is a mutable singleton shared by every test using the same application
	 * context, setting its default request and building from it is done under
	 * {@link #MOCK_MVC_BUILDER_PRIVATE_LOCK MOCK_MVC_BUILDER_PRIVATE_LOCK}. Its default request is left as the last one
	 * set, which is harmless since the {@link MockMvc MockMvc} bean is built beforehand (it would otherwise inherit the
	 * {@code Authorization} header in a lazily initialized context) and this extension always sets its own default
	 * request before building.
	 * <p>
	 * Otherwise, there are no customizations to keep and only Spring Security is applied.
	 */
	private MockMvc createAuthorizedMockMvc(final ExtensionContext context, final Token token) {
		final var webApplicationContext = webApplicationContextOf(context);
		final var defaultRequest = MockMvcRequestBuilders.get("/");
		if (!token.isEmpty()) {
			defaultRequest.header(AUTHORIZATION, token.typedAccessToken());
		}
		return autoConfiguredMockMvcBuilderOf(webApplicationContext)
				.map(builder -> buildUnderLock(webApplicationContext, builder, defaultRequest))
				.orElseGet(() -> MockMvcBuilders.webAppContextSetup(webApplicationContext)
						.apply(SecurityMockMvcConfigurers.springSecurity())
						.defaultRequest(defaultRequest)
						.build());
	}

	private Optional<ConfigurableMockMvcBuilder<?>> autoConfiguredMockMvcBuilderOf(
			final WebApplicationContext webApplicationContext) {
		return Optional.ofNullable(webApplicationContext.getBeanProvider(MockMvcBuilder.class).getIfUnique())
				.filter(ConfigurableMockMvcBuilder.class::isInstance)
				.map(builder -> (ConfigurableMockMvcBuilder<?>) builder);
	}

	private MockMvc buildUnderLock(final WebApplicationContext webApplicationContext,
			final ConfigurableMockMvcBuilder<?> builder, final RequestBuilder defaultRequest) {
		synchronized (MOCK_MVC_BUILDER_PRIVATE_LOCK) {
			// Ensures the MockMvc bean is built from the untouched builder before it is changed
			webApplicationContext.getBeanProvider(MockMvc.class).getIfAvailable();
			builder.defaultRequest(defaultRequest);
			return builder.build();
		}
	}

	private WebApplicationContext webApplicationContextOf(final ExtensionContext context) {
		if (SpringExtension.getApplicationContext(context) instanceof
				final WebApplicationContext webApplicationContext) {
			return webApplicationContext;
		}
		throw new ParameterResolutionException("""
				A MockMvc can only be resolved within a web application context (e.g. @WebMvcTest or @SpringBootTest \
				with a mock web environment)""");
	}

	/**
	 * Safe private lock-splitting approach.
	 *
	 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">
	 * The "Double-Checked Locking is Broken" Declaration
	 * </a>
	 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html">JSR 133 (Java Memory Model) FAQ</a>
	 * @see <a href="https://docs.oracle.com/javase/specs/jls/se24/html/jls-17.html">Chapter 17. Threads and Locks</a>
	 * @see <a href="https://jcp.org/en/jsr/detail?id=133">
	 * JSR 133: JavaTM Memory Model and Thread Specification Revision
	 * </a>
	 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel">The Java Memory Model</a>
	 */
	@SuppressWarnings("java:S2094")
	private static final class MockMvcBuilderPrivateLock {
	}

	private static final class AccessTokenHolder {

		private final ClientCredentialsFlow clientCredentials;

		/**
		 * Safe private lock-splitting approach.
		 */
		private final Object accessTokenHolderPrivateLock = new AccessTokenHolderPrivateLock();

		@SuppressWarnings("java:S3077")
		private volatile @Nullable Token token = null;

		private AccessTokenHolder(final ClientCredentialsFlow clientCredentials) {
			this.clientCredentials = clientCredentials;
		}

		private Token validOrRenew(final Supplier<Optional<Token>> renewal) {
			return valid().orElseGet(() -> renewUnderLock(renewal));
		}

		private Token renewUnderLock(final Supplier<Optional<Token>> renewal) {
			synchronized (accessTokenHolderPrivateLock) {
				var current = token;
				if (requiresNewAccessToken(current)) {
					current = renewal.get().orElse(current);
					token = current;
				}
				return Objects.requireNonNullElse(current, Token.empty());
			}
		}

		/**
		 * Lock-free fast path: a single volatile read.
		 */
		private Optional<Token> valid() {
			return Optional.ofNullable(token)
					.filter(not(Token::expired))
					.map(this::simplyLogThatItIsAValidAccessToken);
		}

		private boolean requiresNewAccessToken(final @Nullable Token token) {
			if (token == null) {
				LOG.debug("The client '{}' is not yet associated with an access token", clientCredentials.clientId());
				return true;
			}
			if (token.expired()) {
				LOG.debug("The client '{}' is associated with an access token that expired in {}",
						clientCredentials.clientId(), token.expiresIn());
				return true;
			}
			simplyLogThatItIsAValidAccessToken(token);
			return false;
		}

		private Token simplyLogThatItIsAValidAccessToken(final Token token) {
			LOG.debug("The client '{}' is associated with a valid access token that expires in {}",
					clientCredentials.clientId(), token.expiresIn());
			return token;
		}

		/**
		 * Safe private lock-splitting approach.
		 *
		 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">
		 * The "Double-Checked Locking is Broken" Declaration
		 * </a>
		 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html">
		 * JSR 133 (Java Memory Model) FAQ
		 * </a>
		 * @see <a href="https://docs.oracle.com/javase/specs/jls/se24/html/jls-17.html">
		 * Chapter 17. Threads and Locks
		 * </a>
		 * @see <a href="https://jcp.org/en/jsr/detail?id=133">
		 * JSR 133: JavaTM Memory Model and Thread Specification Revision
		 * </a>
		 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel">The Java Memory Model</a>
		 */
		@SuppressWarnings("java:S2094")
		private static final class AccessTokenHolderPrivateLock {
		}
	}
}
