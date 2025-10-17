package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.testcontainers.containers.MySQLContainer;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.net.URI;

import com.kinlhp.moname.commons.test.testcontainers.SharedNetwork;
import com.kinlhp.moname.commons.test.testcontainers.UriExtractableContainer;

/**
 * @see KeycloakContainerFactory KeycloakContainerFactory
 */
public abstract class AbstractKeycloakContainerFactory<T extends ExtendableKeycloakContainer<T>>
		implements KeycloakContainerFactory<T> {

	/**
	 * Safe private lock-splitting approach.
	 */
	@Nonnull
	private static final ContainerPrivateLock CONTAINER_PRIVATE_LOCK = new ContainerPrivateLock();

	@Nullable
	@SuppressWarnings("java:S3077")
	private static volatile ExtendableKeycloakContainer<?> container = null;

	@Nonnull
	private final PropertiesKeycloakConnectionDetails keycloakConnectionDetails;

	protected AbstractKeycloakContainerFactory(
			@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
		this.keycloakConnectionDetails = keycloakConnectionDetails;
	}

	@Nonnull
	protected abstract Logger getLogger();

	/**
	 * {@inheritDoc KeycloakContainerFactory}
	 */
	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	public final T getStartedSingleton() {
		// The local variable `readingReducer` is not merely cosmetic. It reduces the number of reads of the volatile
		// field from two or three to one on the fast path. It is a micro-optimization, but it comes for free.
		var readingReducer = container;
		if (readingReducer == null) {
			synchronized (CONTAINER_PRIVATE_LOCK) {
				readingReducer = container;
				if (readingReducer == null) {
					try {
						getLogger().debug(
								"There is no started instance of the Keycloak container, so one will be started");
						readingReducer = create();
						reconfigure((T) readingReducer);
						start((T) readingReducer);
						container = readingReducer;
					} catch (@Nonnull final Exception exception) {
						getLogger().error(
								"Keycloak container startup failed; discarding the partially initialized singleton",
								exception
						);
						container = null;
						throw exception;
					}
				}
			}
		}
		mapProperties((T) readingReducer);
		return (T) readingReducer;
	}

	/**
	 * {@inheritDoc KeycloakContainerFactory}
	 */
	@Override
	public void reconfigure(@Nonnull final T container) {
		reconfigureMemory(container);
		reconfigureNetwork(container, null);
	}

	/**
	 * @see #getStartedSingleton() getStartedSingleton
	 * @see UriExtractableContainer#withAutoResolveTcPatternUriParameters() withAutoResolveTcPatternUriParameters
	 */
	protected final void reconfigureMemory(@Nonnull final T container) {
		requireNotRunning(container);
		if (container instanceof @Nonnull final UriExtractableContainer<?> self) {
			@Nonnull final var issuerUri = requireIssuerUri();
			getLogger().debug("Reconfiguring Keycloak container memory based on the issuer URI {}", issuerUri);
			self.withUri(issuerUri).withAutoResolveTcPatternUriParameters();
		} else {
			getLogger().warn("The container {} ({}) does not support memory reconfiguration via issuer URI parameters",
					container.getClass().getName(), container.getDockerImageName());
		}
	}

	@Nonnull
	private URI requireIssuerUri() {
		try {
			return keycloakConnectionDetails.getIssuerUri();
		} catch (@Nonnull final IllegalArgumentException | NullPointerException exception) {
			@Nonnull final var message = """
					Keycloak connection details are not properly configured; issuer URI \
					(${spring.security.oauth2.resourceserver.jwt.issuer-uri}) must not be null, blank, or about:blank\
					""";
			getLogger().error(message, exception);
			throw new IllegalStateException(message, exception);
		}
	}

	/**
	 * @see #getStartedSingleton() getStartedSingleton
	 */
	protected final <S extends MySQLContainer<S>> void reconfigureNetwork(@Nonnull final T container,
			@Nullable final S mysql) {
		requireNotRunning(container);
		/*
		// TODO: The database testcontainer is not a bean in the Spring context
		getLogger().debug("Reconfiguring Keycloak container to reach MySQL container on network {}",
				mysql.getNetwork().getNetworkName());
		@Nonnull final var kcDbUrlHost = mysql.getContainerName();
		@Nonnull final var kcDbUrlPort = mysql.getMappedPort(MYSQL_PORT);
		keycloak.withEnv("KC_DB_URL_HOST", kcDbUrlHost)
				.withEnv("KC_DB_URL_PORT", kcDbUrlPort.toString())
				.withNetwork(mysql.getNetwork());
		 */
		@Nonnull final var network = SharedNetwork.getSingleton();
		getLogger().debug("Reconfiguring Keycloak container to reach other containers on network {}", network.getNetworkName());
		container.withNetwork(network.getNetwork());
	}

	/**
	 * @see #getStartedSingleton() getStartedSingleton
	 */
	protected final void requireNotRunning(@Nonnull final T container) {
		if (container.isRunning()) {
			@Nonnull final var message = "Container %s (%s) is already running; reconfiguration must precede startup"
					.formatted(container.getClass().getName(), container.getDockerImageName());
			getLogger().error(message);
			throw new IllegalStateException(message);
		}
	}

	/**
	 * {@inheritDoc KeycloakContainerFactory}
	 */
	@Override
	public final void mapProperties(@Nonnull final T container) {
		@Nonnull final var wellKnownEndpoints = KeycloakWellKnownEndpoints
				.from(URI.create(container.getAuthServerUrl()));
		getLogger().debug("""
						Post-processing Keycloak container startup: Mapping from Keycloak container URI {} to Spring \
						Security properties ${spring.security.oauth2.resourceserver.jwt.issuer-uri}, \
						${spring.security.oauth2.resourceserver.jwt.jwk-set-uri} in {} and \
						${spring.security.oauth2.client.provider.keycloak.token-uri} in {}""",
				wellKnownEndpoints.issuerUri(), OAuth2ResourceServerProperties.class.getName(),
				OAuth2ClientProperties.class.getName());
		@Nonnull final var propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
		propertyMapper.from(wellKnownEndpoints.issuerUri()).to(keycloakConnectionDetails::setIssuerUri);
		propertyMapper.from(wellKnownEndpoints.jwkSetUri()).to(keycloakConnectionDetails::setJwkSetUri);
		propertyMapper.from(wellKnownEndpoints.tokenUri()).to(keycloakConnectionDetails::setTokenUri);
	}

	/**
	 * Safe private lock-splitting approach.
	 *
	 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">
	 * The "Double-Checked Locking is Broken" Declaration
	 * </a>
	 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html">JSR 133 (Java Memory Model) FAQ</a>
	 * @see <a href="https://docs.oracle.com/javase/specs/jls/se24/html/jls-17.html">Chapter 17. Threads and Locks</a>
	 * @see <a href="https://jcp.org/en/jsr/detail?id=133">
	 * JSR 133: JavaTM Memory Model and Thread Specification Revision
	 * </a>
	 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel">The Java Memory Model</a>
	 */
	@SuppressWarnings("java:S2094")
	private static final class ContainerPrivateLock {
	}
}
