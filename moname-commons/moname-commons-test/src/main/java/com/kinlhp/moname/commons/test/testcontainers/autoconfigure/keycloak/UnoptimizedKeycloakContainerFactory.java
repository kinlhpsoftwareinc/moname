package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.slf4j.Logger;

import com.kinlhp.moname.commons.test.testcontainers.keycloak.WrappedKeycloakContainer;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MONAME;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.KC_HTTP_RELATIVE_PATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.REALM_RESOURCE_NAME_CLASSPATH;

public class UnoptimizedKeycloakContainerFactory<T extends WrappedKeycloakContainer<T>>
		extends AbstractKeycloakContainerFactory<T> {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UnoptimizedKeycloakContainerFactory.class);

	public UnoptimizedKeycloakContainerFactory(final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
		super(keycloakConnectionDetails);
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	/**
	 * {@inheritDoc KeycloakContainerFactory}
	 */
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
	public void start(final T container) {
		LOG.debug("Starting unoptimized Keycloak container");
		container.start();
	}
}
