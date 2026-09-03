package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import jakarta.annotation.Nonnull;
import java.net.URI;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.KEYCLOAK;

/**
 * @see org.springframework.boot.autoconfigure.kafka.PropertiesKafkaConnectionDetails PropertiesKafkaConnectionDetails
 */
public class PropertiesKeycloakConnectionDetails implements KeycloakConnectionDetails {

	@Nonnull
	private final OAuth2ResourceServerProperties resourceServerProperties;

	@Nonnull
	private final OAuth2ClientProperties clientProperties;

	PropertiesKeycloakConnectionDetails(@Nonnull final OAuth2ResourceServerProperties resourceServerProperties,
			@Nonnull final OAuth2ClientProperties clientProperties) {
		this.resourceServerProperties = resourceServerProperties;
		this.clientProperties = clientProperties;
	}

	@Nonnull
	@Override
	public URI getIssuerUri() {
		return URI.create(resourceServerProperties.getJwt().getIssuerUri());
	}

	@Nonnull
	@Override
	public URI getJwkSetUri() {
		return URI.create(resourceServerProperties.getJwt().getJwkSetUri());
	}

	@Nonnull
	@Override
	public URI getTokenUri() {
		return URI.create(clientProperties.getProvider().get(KEYCLOAK).getTokenUri());
	}

	void setIssuerUri(@Nonnull final URI issuerUri) {
		resourceServerProperties.getJwt().setIssuerUri(issuerUri.toString());
	}

	void setJwkSetUri(@Nonnull final URI jwkSetUri) {
		resourceServerProperties.getJwt().setJwkSetUri(jwkSetUri.toString());
	}

	void setTokenUri(@Nonnull final URI tokenUri) {
		clientProperties.getProvider().get(KEYCLOAK).setTokenUri(tokenUri.toString());
	}
}
