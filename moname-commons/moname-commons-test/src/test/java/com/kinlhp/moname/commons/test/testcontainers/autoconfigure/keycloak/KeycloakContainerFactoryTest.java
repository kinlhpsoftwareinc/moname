package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Nonnull;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("KeycloakContainerFactory")
class KeycloakContainerFactoryTest {

	@DisplayName("declares no instance state")
	@Test
	final void declaresNoInstanceState() {
		@Nonnull final var instanceFields = Arrays.stream(KeycloakContainerFactory.class.getDeclaredFields())
				.filter(field -> !Modifier.isStatic(field.getModifiers()))
				.toList();
		assertThat(instanceFields).isEmpty();
	}

	@DisplayName("is satisfiable by a minimal stateless test double")
	@SuppressWarnings("unchecked")
	@Test
	final <T extends ExtendableKeycloakContainer<T>> void isSatisfiableByAMinimalTestDouble() {
		// A mock avoids constructing a real container, which resolves a Docker image eagerly in its constructor.
		@Nonnull final var mockedContainer = mock(ExtendableKeycloakContainer.class);
		@Nonnull final var factory = new StubKeycloakContainerFactory<T>((T) mockedContainer);
		@Nonnull final var createdContainer = factory.create();
		assertThat(createdContainer).isSameAs(mockedContainer);
		factory.reconfigure((T) mockedContainer);
		factory.mapProperties((T) mockedContainer);
		@Nonnull final var startedSingleton = factory.getStartedSingleton();
		assertThat(startedSingleton).isSameAs(mockedContainer);
	}

	private record StubKeycloakContainerFactory<T extends ExtendableKeycloakContainer<T>>(
			@Nonnull T container) implements KeycloakContainerFactory<T> {

		@Nonnull
		@Override
		public T getStartedSingleton() {
			return container;
		}

		@Nonnull
		@Override
		public T create() {
			return container;
		}

		@Override
		public void reconfigure(@Nonnull final T container) {
			// No-op: this stub verifies contract satisfaction only, not behavior.
		}

		@Override
		public void mapProperties(@Nonnull final T container) {
			// No-op: this stub verifies contract satisfaction only, not behavior.
		}

		@Override
		public void start(@Nonnull final T container) {
			// No-op: this stub verifies contract satisfaction only, not behavior.
		}
	}
}
