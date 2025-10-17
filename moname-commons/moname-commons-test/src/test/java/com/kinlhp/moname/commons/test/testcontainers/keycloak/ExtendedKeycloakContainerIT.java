package com.kinlhp.moname.commons.test.testcontainers.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.test.spring.jpa.AbstractDataJpaTestSliceIT;
import com.kinlhp.moname.commons.test.testcontainers.SharedNetwork;
import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.KeycloakAutoConfiguration;

/**
 * <strong>NOTE:</strong> Inheriting from {@link AbstractDataJpaTestSliceIT AbstractDataJpaTestSliceIT} is mandatory
 * because optimized Keycloak container requires a MySQL instance having {@literal mysql} as hostname on the same bridge
 * network
 */
@ImportAutoConfiguration(KeycloakAutoConfiguration.class)
abstract class ExtendedKeycloakContainerIT<T extends ExtendableKeycloakContainer<T>>
		extends AbstractDataJpaTestSliceIT {

	@Nonnull
	private final T keycloak;

	ExtendedKeycloakContainerIT(@Nonnull final T keycloak) {
		this.keycloak = keycloak;
	}

	abstract void assertInstanceOf();

	@Nonnull
	T getKeycloak() {
		return keycloak;
	}

	@Test
	final void assertIsRunning() {
		Assertions.assertAll(
				() -> Assertions.assertTrue(keycloak.isRunning()),
				this::assertInstanceOf,
				() -> Assertions.assertEquals(SharedNetwork.getSingleton().getNetwork(), getKeycloak().getNetwork())
		);
	}
}
