package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

import jakarta.annotation.Nonnull;
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
}
