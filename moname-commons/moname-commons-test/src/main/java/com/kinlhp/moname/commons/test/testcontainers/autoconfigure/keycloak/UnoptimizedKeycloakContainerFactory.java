package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.test.testcontainers.keycloak.WrappedKeycloakContainer;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MONAME;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.KC_HTTP_RELATIVE_PATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.REALM_RESOURCE_NAME_CLASSPATH;

public class UnoptimizedKeycloakContainerFactory<T extends WrappedKeycloakContainer<T>>
		extends AbstractKeycloakContainerFactory<T> {

	@Nonnull
	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UnoptimizedKeycloakContainerFactory.class);

	public UnoptimizedKeycloakContainerFactory(
			@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
		super(keycloakConnectionDetails);
	}

	@NotNull
	@Override
	protected Logger getLogger() {
		return LOG;
	}

	/**
	 * {@inheritDoc KeycloakContainerFactory}
	 */
	@Nonnull
	@Override
	@SuppressWarnings({"resource", "java:S2095"})
	public T create() {
		LOG.trace("Creating an unoptimized Keycloak container instance");
		//noinspection unchecked
		return (T) new WrappedKeycloakContainer<>()
				.withEnv("KC_BOOTSTRAP_ADMIN_PASSWORD", MONAME)
				.withEnv("KC_BOOTSTRAP_ADMIN_USERNAME", MONAME)
				.withContextPath(KC_HTTP_RELATIVE_PATH)
				.withRealmImportFile(REALM_RESOURCE_NAME_CLASSPATH);
	}

	/**
	 * {@inheritDoc KeycloakContainerFactory}
	 */
	@Override
	public void start(@NotNull final T container) {
		LOG.debug("Starting unoptimized Keycloak container");
		container.start();
	}
}
