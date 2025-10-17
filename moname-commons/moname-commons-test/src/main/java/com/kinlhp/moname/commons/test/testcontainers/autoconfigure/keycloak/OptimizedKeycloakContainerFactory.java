package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.slf4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.testcontainers.containers.MySQLContainer;

import jakarta.annotation.Nonnull;
import java.time.Duration;

import com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer;

public class OptimizedKeycloakContainerFactory<T extends ExtendedKeycloakContainer<T>>
		extends AbstractKeycloakContainerFactory<T> {

	@Nonnull
	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(OptimizedKeycloakContainerFactory.class);

	@Nonnull
	private static final Duration STARTUP_TIMEOUT = Duration.ofMinutes(2L);

	@Nonnull
	private final ObjectProvider<MySQLContainer<?>> mysql;

	public OptimizedKeycloakContainerFactory(
			@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails,
			@Nonnull final ObjectProvider<MySQLContainer<?>> mysql) {
		super(keycloakConnectionDetails);
		this.mysql = mysql;
	}

	@Nonnull
	@Override
	protected Logger getLogger() {
		return LOG;
	}

	/**
	 * {@inheritDoc KeycloakContainerFactory}
	 */
	@Nonnull
	@Override
	public T create() {
		LOG.trace("Creating an optimized Keycloak container instance");
		//noinspection unchecked
		return (T) new ExtendedKeycloakContainer<>();
	}

	/**
	 * {@inheritDoc KeycloakContainerFactory}
	 */
	@Override
	public void reconfigure(@Nonnull final T container) {
		reconfigureMemory(container);
		reconfigureNetworkWithMysql(container);
		container.withStartupTimeout(STARTUP_TIMEOUT);
	}

	@SuppressWarnings("unchecked")
	private <S extends MySQLContainer<S>> void reconfigureNetworkWithMysql(@Nonnull final T container) {
		reconfigureNetwork(container, (S) mysql.getIfAvailable());
	}

	/**
	 * {@inheritDoc KeycloakContainerFactory}
	 */
	@Override
	public void start(@Nonnull final T container) {
		LOG.debug("Starting optimized Keycloak container");
		container.start();
	}
}
