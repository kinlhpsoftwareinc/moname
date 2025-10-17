package com.kinlhp.moname.commons.test.testcontainers.service.connection.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

import jakarta.annotation.Nonnull;
import java.net.URI;

import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.KeycloakConnectionDetails;

import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.REALM_PATH;

/**
 * @see org.springframework.boot.testcontainers.service.connection.kafka.ApacheKafkaContainerConnectionDetailsFactory
 * ApacheKafkaContainerConnectionDetailsFactory
 */
class KeycloakContainerConnectionDetailsFactory<T extends ExtendableKeycloakContainer<T>>
		extends ContainerConnectionDetailsFactory<T, KeycloakConnectionDetails> {

	@Nonnull
	@Override
	protected KeycloakConnectionDetails getContainerConnectionDetails(
			@Nonnull final ContainerConnectionSource<T> source) {
		return new KeycloakContainerConnectionDetails<>(source);
	}

	private static final class KeycloakContainerConnectionDetails<T extends ExtendableKeycloakContainer<T>>
			extends ContainerConnectionDetails<T> implements KeycloakConnectionDetails {

		private KeycloakContainerConnectionDetails(@Nonnull final ContainerConnectionSource<T> source) {
			super(source);
		}

		@Nonnull
		@Override
		public URI getIssuerUri() {
			@Nonnull final var authUri = getContainer().getAuthServerUrl();
			return URI.create("%s%s".formatted(authUri, REALM_PATH));
		}
	}
}
