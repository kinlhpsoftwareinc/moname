package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.testcontainers.containers.Network;

import jakarta.annotation.Nonnull;
import java.time.Duration;

public interface KeycloakContainerFactory<T extends ExtendableKeycloakContainer<T>> {

	/**
	 * It is the <strong>orchestrator template method</strong>.
	 *
	 * @return the started, JVM-wide singleton container.
	 * @implSpec The method {@code getStartedSingleton} performs a specific template-based operation, which is:
	 * <ol>
	 *     <li>
	 *         {@link #create() create} Invoked <strong>at most once per JVM</strong>, as part of the singleton
	 *         lifecycle.
	 *     </li>
	 *     <li>
	 *         {@link #reconfigure(ExtendableKeycloakContainer) reconfigure} Invoked
	 *         <strong>at most once per JVM</strong>, immediately after {@link #create() create}, and always before the
	 *         container is started by {@link #start(ExtendableKeycloakContainer) start}. Testcontainers throws if any
	 *         of the underlying {@link org.testcontainers.containers.GenericContainer#withEnv withEnv} /
	 *         {@link org.testcontainers.containers.GenericContainer#withNetwork(Network) withNetwork} /
	 *         {@link org.testcontainers.containers.GenericContainer#withStartupTimeout(Duration) withStartupTimeout}
	 *         calls are applied to a container that is already running.
	 *     </li>
	 *     <li>
	 *         {@link #start(ExtendableKeycloakContainer) start} Invoked <strong>at most once per JVM</strong>,
	 *         immediately after {@link #reconfigure(ExtendableKeycloakContainer) reconfigure}.
	 *     </li>
	 *     <li>
	 *         {@link #mapProperties(ExtendableKeycloakContainer) mapProperties} Invoked on
	 *         <strong>every application context startup</strong>, including every time the already-running singleton
	 *         container is reused by a new application context.
	 *     </li>
	 * </ol>
	 */
	@Nonnull
	T getStartedSingleton();

	/**
	 * @implSpec See {@link #getStartedSingleton() getStartedSingleton}.
	 */
	@Nonnull
	T create();

	/**
	 * @implSpec See {@link #getStartedSingleton() getStartedSingleton}.
	 */
	void reconfigure(@Nonnull final T container);

	/**
	 * @implSpec See {@link #getStartedSingleton() getStartedSingleton}.
	 */
	void start(@Nonnull final T container);

	/**
	 * @implSpec See {@link #getStartedSingleton() getStartedSingleton}.
	 */
	void mapProperties(@Nonnull final T container);
}
