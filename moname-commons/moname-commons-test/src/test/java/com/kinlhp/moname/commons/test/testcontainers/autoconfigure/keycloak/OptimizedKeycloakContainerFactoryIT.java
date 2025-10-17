package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.testcontainers.containers.MySQLContainer;

import jakarta.annotation.Nonnull;
import java.time.Duration;

import com.kinlhp.moname.commons.test.spring.profile.IfMySqlProfile;
import com.kinlhp.moname.commons.test.testcontainers.SharedNetwork;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.KEYCLOAK;
import static com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.OptimizedKeycloakContainerFactoryIT.EXPRESSION;

@DisabledIf(
		expression = EXPRESSION,
		loadContext = true,
		reason = "Optimized Keycloak factory should only be tested with \"keycloak-optimized\" profile"
)
@DisplayName("OptimizedKeycloakContainerFactory")
@IfMySqlProfile
class OptimizedKeycloakContainerFactoryIT {

	/**
	 * @see org.springframework.core.env.Profiles#of(String...) of
	 */
	@Nonnull
	public static final String EXPRESSION =
			"#{environment.acceptsProfiles(T(org.springframework.core.env.Profiles).of('!keycloak-optimized'))}";

	@DisplayName("configures a 2-minute startup timeout and the shared network, without starting")
	@Test
	final void createsConfiguredNotYetStartedContainer() {
		@Nonnull final ObjectProvider<MySQLContainer<?>> mysql = mock(ObjectProvider.class);
		@Nonnull final var factory = new OptimizedKeycloakContainerFactory(connectionDetails(), mysql);
		try (@Nonnull final var container = factory.create()) {
			factory.reconfigure(container);
			assertThat(container.isRunning()).isFalse();
			assertThat(container.getStartupTimeout()).isEqualTo(Duration.ofMinutes(2L));
			assertThat(container.getNetwork()).isEqualTo(SharedNetwork.getSingleton().getNetwork());
		}
	}

	@Nonnull
	private PropertiesKeycloakConnectionDetails connectionDetails() {
		@Nonnull final var resourceServerProperties = new OAuth2ResourceServerProperties();
		resourceServerProperties.getJwt().setIssuerUri("https://keycloak:8443/auth/realms/moname");
		@Nonnull final var clientProperties = new OAuth2ClientProperties();
		clientProperties.getProvider().put(KEYCLOAK, new OAuth2ClientProperties.Provider());
		return new PropertiesKeycloakConnectionDetails(resourceServerProperties, clientProperties);
	}
}
