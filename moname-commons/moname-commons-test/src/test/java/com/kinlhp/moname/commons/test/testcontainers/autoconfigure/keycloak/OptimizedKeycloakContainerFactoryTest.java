package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.testcontainers.containers.MySQLContainer;

import jakarta.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.KEYCLOAK;

@DisplayName("OptimizedKeycloakContainerFactory")
class OptimizedKeycloakContainerFactoryTest {

	@DisplayName("does not throw at construction when no MySQLContainer bean is available")
	@Test
	final void toleratesAbsentMysql() {
		// Mockito's default answer returns null from getIfAvailable(), simulating no MySQLContainer bean.
		@Nonnull final ObjectProvider<MySQLContainer<?>> mysql = mock(ObjectProvider.class);
		assertThatCode(() -> new OptimizedKeycloakContainerFactory(connectionDetails(), mysql))
				.doesNotThrowAnyException();
	}

	@Nonnull
	private PropertiesKeycloakConnectionDetails connectionDetails() {
		@Nonnull final var clientProperties = new OAuth2ClientProperties();
		clientProperties.getProvider().put(KEYCLOAK, new OAuth2ClientProperties.Provider());
		return new PropertiesKeycloakConnectionDetails(new OAuth2ResourceServerProperties(), clientProperties);
	}
}
