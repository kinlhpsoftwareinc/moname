package com.kinlhp.moname.commons.test.testcontainers.service.connection.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

import java.net.URI;

import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.KeycloakConnectionDetails;

import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.REALM_PATH;

/**
 * @see org.springframework.boot.testcontainers.service.connection.kafka.ApacheKafkaContainerConnectionDetailsFactory
 * ApacheKafkaContainerConnectionDetailsFactory
 */
class KeycloakContainerConnectionDetailsFactory<T extends ExtendableKeycloakContainer<T>>
		extends ContainerConnectionDetailsFactory<T, KeycloakConnectionDetails> {

	@Override
	protected KeycloakConnectionDetails getContainerConnectionDetails(final ContainerConnectionSource<T> source) {
		return new KeycloakContainerConnectionDetails<>(source);
	}

	private static final class KeycloakContainerConnectionDetails<T extends ExtendableKeycloakContainer<T>>
			extends ContainerConnectionDetails<T> implements KeycloakConnectionDetails {

		private KeycloakContainerConnectionDetails(final ContainerConnectionSource<T> source) {
			super(source);
		}

		@Override
		public URI getIssuerUri() {
			final var authUri = getContainer().getAuthServerUrl();
			return URI.create("%s%s".formatted(authUri, REALM_PATH));
		}

		@Override
		public @Nullable SslBundle getSslBundle() {
			return super.getSslBundle();
		}

		@Override
		public @Nullable String getSecurityProtocol() {
			final var sslBundle = getSslBundle();
			return (sslBundle != null) ? sslBundle.getProtocol() : null;
		}
	}
}
