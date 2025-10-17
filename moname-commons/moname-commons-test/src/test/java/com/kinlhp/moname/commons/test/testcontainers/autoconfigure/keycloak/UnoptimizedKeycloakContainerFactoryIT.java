package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.test.context.junit.jupiter.DisabledIf;

import jakarta.annotation.Nonnull;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.KEYCLOAK;
import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MONAME;
import static com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.UnoptimizedKeycloakContainerFactoryIT.EXPRESSION;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.KC_HTTP_RELATIVE_PATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.REALM_RESOURCE_NAME_CLASSPATH;

@DisabledIf(
		expression = EXPRESSION,
		loadContext = true,
		reason = "Unoptimized Keycloak factory should only be tested with database profiles other than \"mysql\""
)
@DisplayName("UnoptimizedKeycloakContainerFactory")
class UnoptimizedKeycloakContainerFactoryIT {

	/**
	 * @see org.springframework.core.env.Profiles#of(String...) of
	 */
	@Nonnull
	public static final String EXPRESSION =
			"#{environment.acceptsProfiles(T(org.springframework.core.env.Profiles).of('mysql'))}";

	@DisplayName("configures env vars, context path, and realm import file, without starting")
	@Test
	final void createsConfiguredNotYetStartedContainer() throws ReflectiveOperationException {
		@Nonnull @SuppressWarnings("rawtypes") final var factory = new UnoptimizedKeycloakContainerFactory(connectionDetails());
		try (@Nonnull final var container = factory.create()) {
			assertThat(container.isRunning()).isFalse();
			assertThat(container.getEnvMap())
					.containsEntry("KC_BOOTSTRAP_ADMIN_PASSWORD", MONAME)
					.containsEntry("KC_BOOTSTRAP_ADMIN_USERNAME", MONAME);
			assertThat(container.getContextPath()).isEqualTo(KC_HTTP_RELATIVE_PATH);
			assertThat(importFiles(container)).containsExactly(REALM_RESOURCE_NAME_CLASSPATH);
		}
	}

	@Nonnull
	private PropertiesKeycloakConnectionDetails connectionDetails() {
		@Nonnull final var clientProperties = new OAuth2ClientProperties();
		clientProperties.getProvider().put(KEYCLOAK, new OAuth2ClientProperties.Provider());
		return new PropertiesKeycloakConnectionDetails(new OAuth2ResourceServerProperties(), clientProperties);
	}

	@Nonnull
	private Set<String> importFiles(@Nonnull final ExtendableKeycloakContainer<?> container)
			throws ReflectiveOperationException {
		@Nonnull final var field = ExtendableKeycloakContainer.class.getDeclaredField("importFiles");
		field.setAccessible(true);
		//noinspection unchecked
		return (Set<String>) field.get(container);
	}
}
