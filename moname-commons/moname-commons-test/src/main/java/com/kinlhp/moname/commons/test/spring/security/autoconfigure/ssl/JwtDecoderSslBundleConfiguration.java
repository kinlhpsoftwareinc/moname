package com.kinlhp.moname.commons.test.spring.security.autoconfigure.ssl;

import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.JwkSetUriJwtDecoderBuilderCustomizer;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.kinlhp.moname.commons.test.spring.security.SslBundleBackedBySsl;

/**
 * @see <a href="https://github.com/spring-projects/spring-boot/issues/45180">
 * Allow configuring an SSLBundle to use when making JWKS requests #45180
 * </a>
 * @see <a href="https://github.com/spring-projects/spring-boot/pull/50280">
 * Allow configuring an SSLBundle to use when making JWKS requests #50280
 * </a>
 * @deprecated since 1.0.0.BUILD-SNAPSHOT for removal in favor of
 * {@link org.springframework.boot.security.oauth2.server.resource.autoconfigure.JwtDecoderConfiguration
 * JwtDecoderConfiguration}
 */
@ConditionalOnClass(JwtDecoder.class)
@ConditionalOnMissingBean(JwtDecoder.class)
@Configuration(proxyBeanMethods = false)
@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
@Profile("test & security & keycloak-optimized")
public class JwtDecoderSslBundleConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(JwtDecoderSslBundleConfiguration.class);

	private static final Duration JWK_SET_CONNECT_TIMEOUT =
			Duration.ofMillis(JWKSourceBuilder.DEFAULT_HTTP_CONNECT_TIMEOUT);

	private static final Duration JWK_SET_READ_TIMEOUT = Duration.ofMillis(JWKSourceBuilder.DEFAULT_HTTP_READ_TIMEOUT);

	private final OAuth2ResourceServerProperties.Jwt jwtProperties;

	private final List<OAuth2TokenValidator<Jwt>> additionalJwtValidators;

	private final ObjectProvider<JwkSetUriJwtDecoderBuilderCustomizer> jwtDecoderBuilderCustomizers;

	private final SslBundle sslBundle;

	JwtDecoderSslBundleConfiguration(final OAuth2ResourceServerProperties resourceServerProperties,
			final ObjectProvider<OAuth2TokenValidator<Jwt>> additionalJwtValidators,
			final ObjectProvider<JwkSetUriJwtDecoderBuilderCustomizer> jwtDecoderBuilderCustomizers,
			final ObjectProvider<ServerProperties> serverProperties,
			final ObjectProvider<SslBundles> sslBundles) {
		jwtProperties = resourceServerProperties.getJwt();
		this.additionalJwtValidators = additionalJwtValidators.orderedStream().toList();
		this.jwtDecoderBuilderCustomizers = jwtDecoderBuilderCustomizers;
		sslBundle = SslBundleBackedBySsl.of(serverProperties, sslBundles);
	}

	@Bean
	@ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
	JwtDecoder jwtDecoderByJwkKeySetUri() {
		final var jwtDecoderBuilder = jwkSetUriJwtDecoderBuilder();
		jwtDecoderBuilderCustomizers.orderedStream().forEach(customizer -> customizer.customize(jwtDecoderBuilder));
		final var jwtDecoder = jwtDecoderBuilder.build();
		jwtDecoder.setJwtValidator(jwtValidators());
		return jwtDecoder;
	}

	private JwkSetUriJwtDecoderBuilder jwkSetUriJwtDecoderBuilder() {
		final var jwkSetUri = jwtProperties.getJwkSetUri();
		LOG.trace("Building JWT (JSON Web Token) decoder for JWKS (JSON Web Key Set) URI {}", jwkSetUri);
		final var jwtDecoderBuilder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).jwsAlgorithms(this::jwsAlgorithms);
		configureSslBundle(jwtDecoderBuilder);
		return jwtDecoderBuilder;
	}

	private void jwsAlgorithms(final Set<SignatureAlgorithm> signatureAlgorithms) {
		final var jwsAlgorithms = jwtProperties.getJwsAlgorithms();
		LOG.trace("Configuring JWS (JSON Web Signature) algorithms {} for JWT (JSON Web Token) decoder", jwsAlgorithms);
		jwsAlgorithms
				.forEach(algorithm -> signatureAlgorithms.add(SignatureAlgorithm.from(algorithm)));
	}

	private void configureSslBundle(final JwkSetUriJwtDecoderBuilder jwtDecoderBuilder) {
		LOG.trace("""
				Configuring an SSLBundle for JWT (JSON Web Token) decoder to be used when making JWKS (JSON Web Key \
				Set) requests""");
		jwtDecoderBuilder.restOperations(sslBundleRestOperations());
	}

	private RestOperations sslBundleRestOperations() {
		LOG.trace("Setting up an HTTP client with SSL context from SSLBundle");
		final var requestFactorySettings = ClientHttpRequestFactorySettings.defaults()
				.withSslBundle(sslBundle)
				.withConnectTimeout(JWK_SET_CONNECT_TIMEOUT)
				.withReadTimeout(JWK_SET_READ_TIMEOUT);
		final var requestFactory = ClientHttpRequestFactoryBuilder.detect().build(requestFactorySettings);
		return new RestTemplate(requestFactory);
	}

	private OAuth2TokenValidator<Jwt> jwtValidators() {
		final var jwtValidators = new ArrayList<OAuth2TokenValidator<Jwt>>();
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

	private JwtClaimValidator<List<String>> audienceValidator(final List<String> audiences) {
		return new JwtClaimValidator<>(JwtClaimNames.AUD, claim -> hasElementsInCommon(claim, audiences));
	}

	private <E> boolean hasElementsInCommon(final @Nullable List<E> c1, final List<E> c2) {
		return c1 != null && !Collections.disjoint(c1, c2);
	}
}
