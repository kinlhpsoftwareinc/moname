package com.kinlhp.moname.commons.test.spring.security.autoconfigure.ssl;

import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.JwkSetUriJwtDecoderBuilderCustomizer;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder.JwkSetUriJwtDecoderBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import javax.net.ssl.SSLParameters;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @see <a href="https://github.com/spring-projects/spring-boot/issues/45180">Allow configuring an SSLBundle to use when making JWKS requests #45180</a>
 * @see <a href="https://github.com/spring-projects/spring-boot/pull/50280">Allow configuring an SSLBundle to use when making JWKS requests #50280</a>
 * @deprecated since 1.0.0.BUILD-SNAPSHOT for removal in favor of
 * {@link org.springframework.boot.security.oauth2.server.resource.autoconfigure.JwtDecoderConfiguration
 * JwtDecoderConfiguration}
 */
@ConditionalOnClass(JwtDecoder.class)
@ConditionalOnMissingBean(JwtDecoder.class)
@Configuration(proxyBeanMethods = false)
@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
public class JwtDecoderSslBundleConfiguration {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(JwtDecoderSslBundleConfiguration.class);

	@Nonnull
	private static final Duration JWK_SET_CONNECT_TIMEOUT =
			Duration.ofMillis(JWKSourceBuilder.DEFAULT_HTTP_CONNECT_TIMEOUT);

	@Nonnull
	private static final Duration JWK_SET_READ_TIMEOUT = Duration.ofMillis(JWKSourceBuilder.DEFAULT_HTTP_READ_TIMEOUT);

	@Nonnull
	private final OAuth2ResourceServerProperties.Jwt jwtProperties;

	@Nonnull
	private final List<OAuth2TokenValidator<Jwt>> additionalJwtValidators;

	@Nonnull
	private final ObjectProvider<JwkSetUriJwtDecoderBuilderCustomizer> jwtDecoderBuilderCustomizers;

	@Nullable
	private final SslBundles sslBundles;

	JwtDecoderSslBundleConfiguration(
			@Nonnull final OAuth2ResourceServerProperties resourceServerProperties,
			@Nonnull final ObjectProvider<OAuth2TokenValidator<Jwt>> additionalJwtValidators,
			@Nonnull final ObjectProvider<JwkSetUriJwtDecoderBuilderCustomizer> jwtDecoderBuilderCustomizers,
			@Nonnull final ObjectProvider<SslBundles> sslBundles) {
		jwtProperties = resourceServerProperties.getJwt();
		this.additionalJwtValidators = additionalJwtValidators.orderedStream().toList();
		this.jwtDecoderBuilderCustomizers = jwtDecoderBuilderCustomizers;
		this.sslBundles = sslBundles.getIfAvailable();
	}

	@Bean
	@ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
	@Nonnull
	JwtDecoder jwtDecoderByJwkKeySetUri() {
		@Nonnull final var jwtDecoderBuilder = jwkSetUriJwtDecoderBuilder();
		jwtDecoderBuilderCustomizers.orderedStream().forEach(customizer -> customizer.customize(jwtDecoderBuilder));
		@Nonnull final var jwtDecoder = jwtDecoderBuilder.build();
		jwtDecoder.setJwtValidator(jwtValidators());
		return jwtDecoder;
	}

	@Nonnull
	private JwkSetUriJwtDecoderBuilder jwkSetUriJwtDecoderBuilder() {
		@Nonnull final var jwkSetUri = jwtProperties.getJwkSetUri();
		LOG.trace("Building JWT (JSON Web Token) decoder for JWKS (JSON Web Key Set) URI {}", jwkSetUri);
		@Nonnull final var jwtDecoderBuilder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
				.jwsAlgorithms(this::jwsAlgorithms);
		configureSslBundle(jwtDecoderBuilder);
		return jwtDecoderBuilder;
	}

	private void jwsAlgorithms(@Nonnull final Set<SignatureAlgorithm> signatureAlgorithms) {
		@Nonnull final var jwsAlgorithms = jwtProperties.getJwsAlgorithms();
		LOG.trace("Configuring JWS (JSON Web Signature) algorithms {} for JWT (JSON Web Token) decoder", jwsAlgorithms);
		jwsAlgorithms
				.forEach(algorithm -> signatureAlgorithms.add(SignatureAlgorithm.from(algorithm)));
	}

	private void configureSslBundle(@Nonnull final JwkSetUriJwtDecoderBuilder jwtDecoderBuilder) {
		LOG.trace("""
				Configuring an SSLBundle for JWT (JSON Web Token) decoder to be used when making JWKS (JSON Web Key \
				Set) requests""");
		jwtDecoderBuilder.restOperations(sslBundleRestOperations());
	}

	@Nonnull
	private RestOperations sslBundleRestOperations() {
		@Nonnull final var requestFactory = new JdkClientHttpRequestFactory(createHttpClient(sslBundle()));
		requestFactory.setReadTimeout(JWK_SET_READ_TIMEOUT);
		return new RestTemplate(requestFactory);
	}

	@Nonnull
	private HttpClient createHttpClient(@Nonnull final SslBundle sslBundle) {
		LOG.trace("Setting up an HTTP client with SSL context from SSLBundle");
		return HttpClient.newBuilder()
				.connectTimeout(JWK_SET_CONNECT_TIMEOUT)
				.sslContext(sslBundle.createSslContext())
				.sslParameters(asSslParameters(sslBundle))
				.build();
	}

	@Nonnull
	private SSLParameters asSslParameters(@Nonnull final SslBundle sslBundle) {
		@Nonnull final var sslOptions = sslBundle.getOptions();
		LOG.trace("Configuring SSL options {} for JWT (JSON Web Token) decoder", sslOptions);
		return new SSLParameters(sslOptions.getCiphers(), sslOptions.getEnabledProtocols());
	}

	@Nonnull
	private SslBundle sslBundle() {
		return Optional.ofNullable(sslBundles)
				.flatMap(bundles -> sslBundleName().map(bundles::getBundle))
				.orElseGet(() -> {
					LOG.trace("No one SSLBundle named ed25519-p12 was found, using system default SSLBundle");
					return SslBundle.systemDefault();
				});
	}

	@Nonnull
	private Optional<String> sslBundleName() {
		@Nonnull final var bundleName = "ed25519-p12";
		LOG.trace("Looking for an SSLBundle named {} to be used when making JWKS (JSON Web Key Set) requests",
				bundleName);
		return Optional.ofNullable(sslBundles)
				.map(SslBundles::getBundleNames)
				.stream()
				.flatMap(Collection::stream)
				.filter(bundleName::equals)
				.findFirst();
	}

	@Nonnull
	private OAuth2TokenValidator<Jwt> jwtValidators() {
		@Nonnull final var jwtValidators = new ArrayList<OAuth2TokenValidator<Jwt>>();
		if (jwtProperties.getIssuerUri() != null) {
			LOG.trace("Adding token validator for \"iss\" (issuer claim) [{}]", jwtProperties.getIssuerUri());
			jwtValidators.add(new JwtIssuerValidator(jwtProperties.getIssuerUri()));
		}
		if (!CollectionUtils.isEmpty(jwtProperties.getAudiences())) {
			LOG.trace("Adding token validator for \"aud\" (audience claim) {}", jwtProperties.getAudiences());
			jwtValidators.add(audienceValidator(jwtProperties.getAudiences()));
		}
		jwtValidators.addAll(additionalJwtValidators);
		return jwtValidators.isEmpty()
				? JwtValidators.createDefault()
				: JwtValidators.createDefaultWithValidators(jwtValidators);
	}

	@Nonnull
	private JwtClaimValidator<List<String>> audienceValidator(@Nonnull final List<String> audiences) {
		return new JwtClaimValidator<>(JwtClaimNames.AUD, claim -> hasElementsInCommon(claim, audiences));
	}

	private <E> boolean hasElementsInCommon(@Nullable final List<E> c1, @Nonnull final List<E> c2) {
		return c1 != null && !Collections.disjoint(c1, c2);
	}
}
