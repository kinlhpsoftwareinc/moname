package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.net.URI;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.KEYCLOAK;

/**
 * @see org.springframework.boot.autoconfigure.kafka.PropertiesKafkaConnectionDetails PropertiesKafkaConnectionDetails
 */
public class PropertiesKeycloakConnectionDetails implements KeycloakConnectionDetails {

	private final OAuth2ResourceServerProperties resourceServerProperties;

	private final OAuth2ClientProperties clientProperties;

	PropertiesKeycloakConnectionDetails(final OAuth2ResourceServerProperties resourceServerProperties,
			final OAuth2ClientProperties clientProperties) {
		this.resourceServerProperties = resourceServerProperties;
		this.clientProperties = clientProperties;
	}

	@Override
	public URI getIssuerUri() {
		return URI.create(resourceServerProperties.getJwt().getIssuerUri());
	}

	@Override
	public URI getJwkSetUri() {
		return URI.create(resourceServerProperties.getJwt().getJwkSetUri());
	}

	@Override
	public URI getTokenUri() {
		return URI.create(clientProperties.getProvider().get(KEYCLOAK).getTokenUri());
	}

	void setIssuerUri(final URI issuerUri) {
		resourceServerProperties.getJwt().setIssuerUri(issuerUri.toString());
	}

	void setJwkSetUri(final URI jwkSetUri) {
		resourceServerProperties.getJwt().setJwkSetUri(jwkSetUri.toString());
	}

	void setTokenUri(final URI tokenUri) {
		clientProperties.getProvider().get(KEYCLOAK).setTokenUri(tokenUri.toString());
	}
}
