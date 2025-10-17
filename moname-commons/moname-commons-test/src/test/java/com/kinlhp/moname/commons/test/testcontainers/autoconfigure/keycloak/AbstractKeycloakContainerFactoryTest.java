package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import jakarta.annotation.Nonnull;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.kinlhp.moname.commons.test.testcontainers.keycloak.WrappedKeycloakContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.KEYCLOAK;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.DEFAULT_ISSUER_URI;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.KC_HTTPS_PORT;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.KC_HTTP_RELATIVE_PATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.KC_HTTP_RELATIVE_PATH_PATTERN;

@DisplayName("AbstractKeycloakContainerFactory")
class AbstractKeycloakContainerFactoryTest {

	@AfterEach
	@BeforeEach
	final void resetSingleton() throws ReflectiveOperationException {
		@Nonnull final var field = AbstractKeycloakContainerFactory.class.getDeclaredField("container");
		field.setAccessible(true);
		field.set(null, null);
	}

	@DisplayName("creates and reconfigures the container once, but maps properties on every call")
	@SuppressWarnings("unchecked")
	@Test
	final void createsOnceButMapsPropertiesEveryCall() {
		@Nonnull final var mockedContainer = mockContainer();
		@Nonnull @SuppressWarnings("rawtypes") final var factory =
				spy(new TestableKeycloakContainerFactory(connectionDetails(), mockedContainer));
		factory.getStartedSingleton();
		factory.getStartedSingleton();
		//noinspection resource
		verify(factory, times(1)).create();
		verify(factory, times(1)).reconfigure(any());
		verify(factory, times(1)).start(any());
		verify(mockedContainer, times(1)).start();
		verify(factory, times(2)).mapProperties(any());
	}

	@DisplayName("discards the singleton and rethrows when startup fails")
	@Test
	final void discardsSingletonOnStartupFailure() {
		@Nonnull final var mockedContainer = mockContainer();
		doThrow(new IllegalStateException("Simulated container startup failure")).when(mockedContainer).start();
		@Nonnull @SuppressWarnings({"rawtypes", "unchecked"}) final var factory =
				new TestableKeycloakContainerFactory(connectionDetails(), mockedContainer);
		assertThatThrownBy(factory::getStartedSingleton)
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("Simulated container startup failure");
	}

	@DisplayName("throws when try to reconfigure the container is already running")
	@SuppressWarnings("unchecked")
	@Test
	final void throwsOnReconfiguringARunningContainer() {
		@Nonnull final var mockedContainer = mockContainer();
		when(mockedContainer.isRunning()).thenReturn(true);
		@Nonnull @SuppressWarnings("rawtypes") final var factory =
				new TestableKeycloakContainerFactory(connectionDetails(), mockedContainer);
		assertThatThrownBy(() -> factory.reconfigureMemory(mockedContainer))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining(" is already running; reconfiguration must precede startup");
	}

	@DisplayName("logs a WARN and does not throw when the container is not a UriExtractableContainer")
	@SuppressWarnings("unchecked")
	@Test
	final void warnsWithoutThrowingForNonUriExtractableContainer() {
		@Nonnull final var mockedContainer = mockContainer();
		@Nonnull @SuppressWarnings("rawtypes") final var factory =
				new TestableKeycloakContainerFactory(connectionDetails(), mockedContainer);
		@Nonnull final var logAppender = new ListAppender<ILoggingEvent>();
		logAppender.start();
		@Nonnull final var logger = (ch.qos.logback.classic.Logger) factory.getLogger();
		logger.addAppender(logAppender);
		try {
			assertThatCode(() -> factory.reconfigureMemory(mockedContainer)).doesNotThrowAnyException();
			assertThat(logAppender.list).anySatisfy(event -> {
				assertThat(event.getLevel()).isEqualTo(Level.WARN);
				assertThat(event.getFormattedMessage())
						.contains("The container ")
						.contains(" does not support memory reconfiguration via issuer URI parameters");
			});
		} finally {
			logger.detachAppender(logAppender);
		}
	}

	@DisplayName("rejects a missing issuer URI property, naming the property key")
	@SuppressWarnings("unchecked")
	@Test
	final void rejectsMissingIssuerUri() {
		@Nonnull final var mockedContainer = mock(WrappedKeycloakContainer.class);
		@Nonnull @SuppressWarnings("rawtypes") final var factory =
				new TestableKeycloakContainerFactory(connectionDetailsWithoutIssuerUri(), mockedContainer);
		assertThatThrownBy(() -> factory.reconfigureMemory(mockedContainer))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("""
						Keycloak connection details are not properly configured; issuer URI \
						(${spring.security.oauth2.resourceserver.jwt.issuer-uri}) must not be null, blank, or \
						about:blank""");
	}

	@DisplayName("creates the container exactly once under concurrent access")
	@Test
	final void createsExactlyOnceUnderConcurrency() throws InterruptedException {
		@Nonnull final var mockedContainer = mockContainer();
		@Nonnull @SuppressWarnings({"rawtypes", "unchecked"}) final var factory =
				spy(new TestableKeycloakContainerFactory(connectionDetails(), mockedContainer));
		final int threadCount = 16;
		@Nonnull final var readyLatch = new CountDownLatch(threadCount);
		@Nonnull final var startLatch = new CountDownLatch(1);
		@Nonnull final var threadPool = Executors.newFixedThreadPool(threadCount);
		try (@Nonnull final var pool = threadPool) {
			for (int i = 0; i < threadCount; i++) {
				pool.submit(() -> {
					readyLatch.countDown();
					try {
						startLatch.await();
					} catch (@Nonnull final InterruptedException exception) {
						Thread.currentThread().interrupt();
					}
					factory.getStartedSingleton();
				});
			}
			readyLatch.await();
			startLatch.countDown();
		} finally {
			//noinspection ResultOfMethodCallIgnored
			threadPool.awaitTermination(10L, TimeUnit.SECONDS);
		}
		//noinspection resource
		verify(factory, times(1)).create();
	}

	@Nonnull
	private <T extends ExtendableKeycloakContainer<T>> T mockContainer() {
		@Nonnull final var mockedContainer = mock(ExtendableKeycloakContainer.class);
		@Nonnull final var authServerUrl = KC_HTTP_RELATIVE_PATH_PATTERN.formatted(KEYCLOAK, KC_HTTPS_PORT, KC_HTTP_RELATIVE_PATH);
		when(mockedContainer.getAuthServerUrl()).thenReturn(authServerUrl);
		//noinspection unchecked
		return (T) mockedContainer;
	}

	@Nonnull
	private PropertiesKeycloakConnectionDetails connectionDetails() {
		@Nonnull final var resourceServerProperties = new OAuth2ResourceServerProperties();
		resourceServerProperties.getJwt().setIssuerUri(DEFAULT_ISSUER_URI.toString());
		return new PropertiesKeycloakConnectionDetails(resourceServerProperties, clientProperties());
	}

	@Nonnull
	private PropertiesKeycloakConnectionDetails connectionDetailsWithoutIssuerUri() {
		return new PropertiesKeycloakConnectionDetails(new OAuth2ResourceServerProperties(), clientProperties());
	}

	@Nonnull
	private OAuth2ClientProperties clientProperties() {
		@Nonnull final var clientProperties = new OAuth2ClientProperties();
		clientProperties.getProvider().put(KEYCLOAK, new OAuth2ClientProperties.Provider());
		return clientProperties;
	}

	private static final class TestableKeycloakContainerFactory<T extends ExtendableKeycloakContainer<T>>
			extends AbstractKeycloakContainerFactory<T> {

		@Nonnull
		private final T container;

		TestableKeycloakContainerFactory(@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails,
				@Nonnull final T container) {
			super(keycloakConnectionDetails);
			this.container = container;
		}

		@Nonnull
		@Override
		protected Logger getLogger() {
			return LoggerFactory.getLogger(TestableKeycloakContainerFactory.class);
		}

		@Nonnull
		@Override
		public T create() {
			return container;
		}

		@Override
		public void reconfigure(@Nonnull final T container) {
			// No-op: the lifecycle tests exercise the template, not memory/network reconfiguration (covered directly,
			// separately, by calling the protected helpers).
		}

		@Override
		public void start(@Nonnull final T container) {
			getLogger().debug("Starting mocked Keycloak container");
			container.start();
		}
	}
}
