package com.kinlhp.moname.commons.test.spring.security;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.ssl.SslAutoConfiguration;
import org.springframework.boot.ssl.NoSuchSslBundleException;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.annotation.Nonnull;
import javax.net.ssl.SSLContext;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.kinlhp.moname.commons.test.spring.security.ClientCredentialsFlow.Token;
import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.KeycloakAutoConfiguration;
import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.PropertiesKeycloakConnectionDetails;

import static jakarta.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static jakarta.ws.rs.core.Response.Status.OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class ClientCredentialsFlowExtension implements BeforeTestExecutionCallback {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(ClientCredentialsFlowExtension.class);

	@Nonnull
	private static final Map<ClientCredentialsFlow, Token> CACHE = new ConcurrentHashMap<>();

	@Override
	public void beforeTestExecution(@Nonnull final ExtensionContext context) {
		if (isClientCredentialsFlow().and(shouldRequestAccessToken()).and(canRequestAccessToken()).test(context)) {
			requestAccessToken(context);
		}
		wrapMockMvc(context);
	}

	@Nonnull
	private Predicate<ExtensionContext> isClientCredentialsFlow() {
		return isClientCredentialsFlowAnnotated().and(isTestInstancePresent());
	}

	@Nonnull
	private Predicate<ExtensionContext> isClientCredentialsFlowAnnotated() {
		return context -> context.getTestMethod().filter(this::isClientCredentialsFlowAnnotated).isPresent();
	}

	private boolean isClientCredentialsFlowAnnotated(@Nonnull final Method testMethod) {
		final var isAnnotated = testMethod.isAnnotationPresent(ClientCredentialsFlow.class);
		if (!isAnnotated) {
			LOG.warn("It is not a @ClientCredentialsFlow annotated method");
		}
		return isAnnotated;
	}

	@Nonnull
	private Predicate<ExtensionContext> isTestInstancePresent() {
		// TODO: Log
		return context -> context.getTestInstance().isPresent();
	}

	@Nonnull
	private Predicate<ExtensionContext> shouldRequestAccessToken() {
		return context -> requiresNewAccessToken().test(clientCredentialsFlowOf(context));
	}

	@Nonnull
	private Predicate<ClientCredentialsFlow> requiresNewAccessToken() {
		return isAccessTokenAbsent().or(isAccessTokenExpired()).or(simplyLogThatItIsAValidAccessToken());
	}

	@Nonnull
	private Predicate<ClientCredentialsFlow> isAccessTokenAbsent() {
		return clientCredentials -> {
			@Nonnull final var absent = new AtomicBoolean(false);
			CACHE.computeIfAbsent(clientCredentials, unused -> {
				LOG.debug("The client '{}' is not yet associated with an access token", clientCredentials.clientId());
				absent.set(true);
				return null;
			});
			return absent.get();
		};
	}

	@Nonnull
	private Predicate<ClientCredentialsFlow> isAccessTokenExpired() {
		return clientCredentials -> {
			@Nonnull final var expired = new AtomicBoolean(false);
			CACHE.computeIfPresent(clientCredentials, (unused, token) -> {
				if (token.expired()) {
					LOG.debug(
							"The client '{}' is associated with an access token that expired in {}",
							clientCredentials.clientId(),
							token.expiresIn()
					);
					expired.set(true);
				}
				return token;
			});
			return expired.get();
		};
	}

	@Nonnull
	private Predicate<ClientCredentialsFlow> simplyLogThatItIsAValidAccessToken() {
		return clientCredentials -> {
			LOG.debug("The client '{}' is associated with a valid access token that expires in {}",
					clientCredentials.clientId(), CACHE.get(clientCredentials).expiresIn());
			return false;
		};
	}

	@Nonnull
	private ClientCredentialsFlow clientCredentialsFlowOf(@Nonnull final ExtensionContext context) {
		return context.getTestMethod()
				.map(testMethod -> testMethod.getAnnotation(ClientCredentialsFlow.class))
				.orElseThrow(() -> new NoSuchElementException("It is not a @ClientCredentialsFlow annotated method"));
	}

	@Nonnull
	private Predicate<ExtensionContext> canRequestAccessToken() {
		return runningKeycloak().and(hasKeycloakConnectionDetails());
	}

	@Nonnull
	private Predicate<ExtensionContext> runningKeycloak() {
		// TODO: Log
		return context -> SpringExtension.getApplicationContext(context)
				.containsBeanDefinition(ExtendableKeycloakContainer.class.getName());
	}

	@Nonnull
	private Predicate<ExtensionContext> hasKeycloakConnectionDetails() {
		// TODO: Log
		return context -> SpringExtension.getApplicationContext(context)
				.containsBeanDefinition(KeycloakAutoConfiguration.class.getName());
	}

	private void requestAccessToken(@Nonnull final ExtensionContext context) {
		@Nonnull final var keycloakConnectionDetails = propertiesKeycloakConnectionDetailsOf(context);
		@Nonnull final var clientCredentials = clientCredentialsFlowOf(context);
		CACHE.put(clientCredentials, sendAccessTokenRequest(context, keycloakConnectionDetails, clientCredentials));
	}

	@Nonnull
	private PropertiesKeycloakConnectionDetails propertiesKeycloakConnectionDetailsOf(
			@Nonnull final ExtensionContext context) {
		return SpringExtension.getApplicationContext(context).getBean(PropertiesKeycloakConnectionDetails.class);
	}

	@Nonnull
	private Token sendAccessTokenRequest(@Nonnull final ExtensionContext context,
			@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails,
			@Nonnull final ClientCredentialsFlow clientCredentials) {
		try (@Nonnull final var httpClient = httpClientBuilder(context).build()) {
			@Nonnull final var httpRequest = buildHttpRequest(keycloakConnectionDetails, clientCredentials);
			LOG.trace("Requesting a new access token for client '{}' pointing to {}",
					clientCredentials.clientId(), keycloakConnectionDetails.getTokenUri());
			@Nonnull final var httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());
			return extractAccessToken(httpResponse, clientCredentials);
		} catch (@Nonnull final Exception exception) {
			LOG.error("// TODO: Log", exception); // TODO: Log
			throw new RuntimeException("// TODO: Message", exception); // TODO: Message
		}
	}

	@Nonnull
	private HttpClient.Builder httpClientBuilder(@Nonnull final ExtensionContext context) {
		return sslContextOf(context).map(this::tlsHttpClientBuilder).orElseGet(this::httpClientBuilder);
	}

	@Nonnull
	private Optional<SSLContext> sslContextOf(@Nonnull final ExtensionContext context) {
		try {
			return sslBundlesOf(context).map(sslBundles -> sslBundles.getBundle("ed25519-p12").createSslContext());
			//return sslBundlesOf(context).map(sslBundles -> sslBundles.getBundle("rsa-p12").createSslContext());
		} catch (@Nonnull final NoSuchSslBundleException exception) {
			// TODO: Do a more elegant approach
			LOG.warn("// TODO: Log"); // TODO: Log
			exception.printStackTrace();
			return Optional.empty();
		}
	}

	@Nonnull
	private Optional<SslBundles> sslBundlesOf(@Nonnull final ExtensionContext context) {
		// TODO: Log
		return Optional.of(SpringExtension.getApplicationContext(context))
				.filter(applicationContext ->
						applicationContext.containsBeanDefinition(SslAutoConfiguration.class.getName()))
				.map(applicationContext -> applicationContext.getBean(SslBundles.class));
	}

	@Nonnull
	private HttpClient.Builder tlsHttpClientBuilder(@Nonnull final SSLContext sslContext) {
		// TODO: Log
		return httpClientBuilder().sslContext(sslContext);
	}

	@Nonnull
	private HttpClient.Builder httpClientBuilder() {
		// TODO: Log
		return HttpClient.newBuilder();
	}

	@Nonnull
	private HttpRequest buildHttpRequest(@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails,
			@Nonnull final ClientCredentialsFlow clientCredentials) {
		return HttpRequest.newBuilder(keycloakConnectionDetails.getTokenUri())
				.header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED)
				.POST(BodyPublishers.ofString(createXWwwFormUrlencoded(clientCredentials)))
				.build();
	}

	@Nonnull
	private String createXWwwFormUrlencoded(@Nonnull final ClientCredentialsFlow clientCredentialsFlow) {
		return Map.of(
						"client_id", clientCredentialsFlow.clientId(),
						"client_secret", clientCredentialsFlow.clientSecret(),
						"grant_type", clientCredentialsFlow.grantType()
				)
				.entrySet().stream()
				.map(param -> String.join("=", param.getKey(), URLEncoder.encode(param.getValue(), UTF_8)))
				.collect(Collectors.joining("&"));
	}

	@Nonnull
	private Token extractAccessToken(@Nonnull final HttpResponse<String> httpResponse,
			@Nonnull final ClientCredentialsFlow clientCredentials) {
		// TODO: To be refactored in favor of use some design pattern (maybe strategy pattern)
		return httpResponse.statusCode() == OK.getStatusCode()
				? doExtractAccessToken(httpResponse)
				: createEmptyAccessToken(httpResponse, clientCredentials);
	}

	@Nonnull
	private Token doExtractAccessToken(@Nonnull final HttpResponse<String> httpResponse) {
		try {
			@Nonnull final var accessToken = new ObjectMapper().readValue(httpResponse.body(), Token.class);
			LOG.debug(
					"{} token successfully obtained and expires at {}",
					accessToken.tokenType(),
					accessToken.expiresIn()
			);
			return accessToken;
		} catch (@Nonnull final JacksonException exception) {
			LOG.error("// TODO: Log", exception); // TODO: Log
			throw new RuntimeException("// TODO: Message", exception); // TODO: Message
		}
	}

	@Nonnull
	@SuppressWarnings("java:S2629")
	private Token createEmptyAccessToken(@Nonnull final HttpResponse<String> httpResponse,
			@Nonnull final ClientCredentialsFlow clientCredentials) {
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

	@SuppressWarnings("java:S3011")
	private void wrapMockMvc(@Nonnull final ExtensionContext context) {
		@Nonnull final var testInstance = context.getRequiredTestInstance();
		Arrays.stream(testInstance.getClass().getDeclaredFields())
				.filter(field -> MockMvc.class.equals(field.getType()))
				.forEach(field -> {
					field.setAccessible(true);
					try {
						@Nonnull final var mockMvc = (MockMvc) field.get(testInstance);
						field.set(testInstance, createWrappedMockMvc(mockMvc, context));
					} catch (@Nonnull final IllegalAccessException exception) {
						LOG.error("// TODO: Log", exception); // TODO: Log
						throw new RuntimeException("// TODO: Message", exception); // TODO: Message
					}
				});
		// TODO: Podem haver mais que um atributo do tipo MockMvc:
		//  - o que fazer quando houver mais do que um?
		//  - o que fazer quando nem todos precisarem do header Authorization? (@ClientCredentialsFlowSettable para diferencia-los?)
	}

	@Nonnull
	@SuppressWarnings("java:S4449")
	private MockMvc createWrappedMockMvc(@Nonnull final MockMvc mockMvc, @Nonnull final ExtensionContext context) {
		@Nonnull final var token = CACHE.get(clientCredentialsFlowOf(context));
		return MockMvcBuilders.webAppContextSetup(mockMvc.getDispatcherServlet().getWebApplicationContext())
				.apply(springSecurity())
				.defaultRequest(MockMvcRequestBuilders.get("/").header(AUTHORIZATION, token.typedAccessToken()))
				.build();
	}
}
