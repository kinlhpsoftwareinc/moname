package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;
import org.springframework.boot.ssl.SslBundle;

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
	URI getIssuerUri();

	/**
	 * @see "${spring.security.oauth2.resourceserver.jwt.jwk-set-uri} in application-security-yaml and/or
	 * application-keycloak.yaml"
	 */
	default URI getJwkSetUri() {
		return URI.create("%s%s".formatted(getIssuerUri(), JWKS_PATH));
	}

	/**
	 * @see "${spring.security.oauth2.client.provider.keycloak.token-uri} in application-security.yaml and/or
	 * application-keycloak-yaml"
	 */
	default URI getTokenUri() {
		return URI.create("%s%s".formatted(getIssuerUri(), TOKEN_ENDPOINT_PATH));
	}

	default @Nullable SslBundle getSslBundle() {
		return null;
	}

	default @Nullable String getSecurityProtocol() {
		return null;
	}

	/**
	 * Keycloak connection details configuration.
	 */
	interface Configuration {

		URI getIssuerUri();

		default @Nullable SslBundle getSslBundle() {
			return null;
		}

		default @Nullable String getSecurityProtocol() {
			return null;
		}

		/**
		 * Creates a new configuration with the given issuer URI.
		 *
		 * @return the configuration
		 */
		static Configuration of(final URI issuerUri) {
			return Configuration.of(issuerUri, null, null);
		}

		/**
		 * Creates a new configuration with the given issuer URI and SSL bundle.
		 *
		 * @return the configuration
		 */
		static Configuration of(final URI issuerUri, final SslBundle sslBundle) {
			return Configuration.of(issuerUri, sslBundle, null);
		}

		/**
		 * Creates a new configuration with the given issuer URI, SSL bundle and security protocol.
		 *
		 * @return the configuration
		 */
		static Configuration of(final URI issuerUri, final @Nullable SslBundle sslBundle,
				final @Nullable String securityProtocol) {
			return new Configuration() {

				@Override
				public URI getIssuerUri() {
					return issuerUri;
				}

				@Override
				public @Nullable SslBundle getSslBundle() {
					return sslBundle;
				}

				@Override
				public @Nullable String getSecurityProtocol() {
					return securityProtocol;
				}
			};
		}
	}
}
