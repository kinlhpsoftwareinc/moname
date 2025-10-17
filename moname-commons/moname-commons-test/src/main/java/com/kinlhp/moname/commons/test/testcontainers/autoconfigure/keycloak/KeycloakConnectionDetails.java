package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;
import org.springframework.boot.ssl.SslBundle;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.net.URI;

import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.JWKS_PATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.TOKEN_ENDPOINT_PATH;

/**
 * @see org.springframework.boot.autoconfigure.kafka.KafkaConnectionDetails KafkaConnectionDetails
 */
public interface KeycloakConnectionDetails extends ConnectionDetails {

	/**
	 * @see "${spring.security.oauth2.resourceserver.jwt.issuer-uri} in application-security.yaml and/or
	 * application-keycloak.yaml"
	 */
	@Nonnull
	URI getIssuerUri();

	/**
	 * @see "${spring.security.oauth2.resourceserver.jwt.jwk-set-uri} in application-security-yaml and/or
	 * application-keycloak.yaml"
	 */
	@Nonnull
	default URI getJwkSetUri() {
		return URI.create("%s%s".formatted(getIssuerUri(), JWKS_PATH));
	}

	/**
	 * @see "${spring.security.oauth2.client.provider.keycloak.token-uri} in application-security.yaml and/or
	 * application-keycloak-yaml"
	 */
	@Nonnull
	default URI getTokenUri() {
		return URI.create("%s%s".formatted(getIssuerUri(), TOKEN_ENDPOINT_PATH));
	}

	@Nullable
	default SslBundle getSslBundle() {
		return null;
	}

	@Nullable
	default String getSecurityProtocol() {
		return null;
	}

	/**
	 * Keycloak connection details configuration.
	 */
	interface Configuration {

		@Nonnull
		URI getIssuerUri();

		@Nullable
		default SslBundle getSslBundle() {
			return null;
		}

		@Nullable
		default String getSecurityProtocol() {
			return null;
		}

		/**
		 * Creates a new configuration with the given issuer URI.
		 *
		 * @return the configuration
		 */
		@Nonnull
		static Configuration of(@Nonnull final URI issuerUri) {
			return Configuration.of(issuerUri, null, null);
		}

		/**
		 * Creates a new configuration with the given issuer URI and SSL bundle.
		 *
		 * @return the configuration
		 */
		@Nonnull
		static Configuration of(@Nonnull final URI issuerUri, @Nonnull final SslBundle sslBundle) {
			return Configuration.of(issuerUri, sslBundle, null);
		}

		/**
		 * Creates a new configuration with the given issuer URI, SSL bundle and security protocol.
		 *
		 * @return the configuration
		 */
		@Nonnull
		static Configuration of(@Nonnull final URI issuerUri, @Nullable final SslBundle sslBundle,
				@Nullable final String securityProtocol) {
			return new Configuration() {

				@Nonnull
				@Override
				public URI getIssuerUri() {
					return issuerUri;
				}

				@Nullable
				@Override
				public SslBundle getSslBundle() {
					return sslBundle;
				}

				@Nullable
				@Override
				public String getSecurityProtocol() {
					return securityProtocol;
				}
			};
		}
	}
}
