package com.kinlhp.moname.commons.test.spring.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.ssl.SslAutoConfiguration;
import org.springframework.boot.test.system.CapturedOutput;

import jakarta.annotation.Nonnull;
import java.util.stream.Stream;

import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.KeycloakAutoConfiguration;
import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.PropertiesKeycloakConnectionDetails;

final class ClientCredentialsFlowExtensionPlatformLauncherIT
		extends AbstractClientCredentialsFlowExtensionPlatformLauncherIT {

	@Nonnull
	private static Stream<Class<? extends ClassToBeTested>> factoryMethod() {
		return Stream.of(
				ClientCredentialsIT.class,
				ClientCredentialsExplicitAlreadyCachedIT.class,
				ClientCredentialsExplicitNonExistentIT.class
		);
	}

	@Override
	protected void assertTestExecutionSummary(@Nonnull final TestExecutionSummary summary) {
		Assertions.assertThat(summary.getTestsSucceededCount()).isEqualTo(1L);
	}

	@EnabledIfSystemProperty(
			disabledReason = TWICE_EXECUTION_WORKAROUND_DISABLED_REASON,
			matches = TWICE_EXECUTION_WORKAROUND_PROPERTY_VALUE,
			named = TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY
	) // TODO: Fix twice execution: https://stackoverflow.com/q/72911486
	@ImportAutoConfiguration({KeycloakAutoConfiguration.class, SslAutoConfiguration.class})
	static final class ClientCredentialsIT implements ClassToBeTested {

		/**
		 * <strong>NOTE:</strong> Flaky with {@link
		 * com.kinlhp.moname.commons.test.spring.security.ClientCredentialsFlowExtensionPlatformTestKitIT.ClientCredentialsIT#methodToBeTested
		 * methodToBeTested}
		 */
		@ClientCredentialsFlow
		@Override
		@Test
		public void methodToBeTested(@Nonnull final CapturedOutput output,
				@Autowired @Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
			org.junit.jupiter.api.Assertions.assertAll(
					() -> Assertions.assertThat(output.getOut()).contains(
							"The client 'moname-commons-api' is not yet associated with an access token"),
					() -> Assertions.assertThat(output.getOut()).contains(
							"Requesting a new access token for client 'moname-commons-api' pointing to %s"
									.formatted(keycloakConnectionDetails.getTokenUri())),
					() -> Assertions.assertThat(output.getOut()).contains(
							"Bearer token successfully obtained and expires at ")
			);
		}
	}

	@EnabledIfSystemProperty(
			disabledReason = TWICE_EXECUTION_WORKAROUND_DISABLED_REASON,
			matches = TWICE_EXECUTION_WORKAROUND_PROPERTY_VALUE,
			named = TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY
	) // TODO: Fix twice execution: https://stackoverflow.com/q/72911486
	@ImportAutoConfiguration({KeycloakAutoConfiguration.class, SslAutoConfiguration.class})
	static final class ClientCredentialsExplicitAlreadyCachedIT implements ClassToBeTested {

		@ClientCredentialsFlow(
				clientId = "moname-commons-api",
				clientSecret = "moname",
				grantType = "client_credentials"
		)
		@Override
		@Test
		public void methodToBeTested(@Nonnull final CapturedOutput output,
				@Autowired @Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
			org.junit.jupiter.api.Assertions.assertAll(
					() -> Assertions.assertThat(output.getOut()).contains(
							"The client 'moname-commons-api' is associated with a valid access token that expires in "),
					() -> Assertions.assertThat(output.getOut()).doesNotContain(
							"Requesting a new access token for client 'moname-commons-api' pointing to "),
					() -> Assertions.assertThat(output.getOut()).doesNotContain(
							"Bearer token successfully obtained and expires at ")
			);
		}
	}

	@EnabledIfSystemProperty(
			disabledReason = TWICE_EXECUTION_WORKAROUND_DISABLED_REASON,
			matches = TWICE_EXECUTION_WORKAROUND_PROPERTY_VALUE,
			named = TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY
	) // TODO: Fix twice execution: https://stackoverflow.com/q/72911486
	@ImportAutoConfiguration({KeycloakAutoConfiguration.class, SslAutoConfiguration.class})
	static final class ClientCredentialsExplicitNonExistentIT implements ClassToBeTested {

		private static final String CLIENT_ID = "admin@" + TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY;

		@ClientCredentialsFlow(
				clientId = CLIENT_ID,
				clientSecret = "admin",
				grantType = "client_credentials"
		)
		@Override
		@Test
		public void methodToBeTested(@Nonnull final CapturedOutput output,
				@Autowired @Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
			@Nonnull final var tokenUri = keycloakConnectionDetails.getTokenUri();
			org.junit.jupiter.api.Assertions.assertAll(
					() -> Assertions.assertThat(output.getOut()).contains(
							"The client '%s' is not yet associated with an access token".formatted(CLIENT_ID)),
					() -> Assertions.assertThat(output.getOut()).contains(
							"Requesting a new access token for client '%s' pointing to %s"
									.formatted(CLIENT_ID, tokenUri)),
					() -> Assertions.assertThat(output.getOut()).doesNotContain(
							"Bearer token successfully obtained and expires at "),
					() -> Assertions.assertThat(output.getOut()).contains("""
							type="CLIENT_LOGIN_ERROR", realmId="b5ad5326-5378-421f-a883-4dc292221c95", realmName="monam\
							e", clientId="%s", userId="null", ipAddress=\"""".formatted(CLIENT_ID)),
					() -> Assertions.assertThat(output.getOut()).contains("""
							", error="client_not_found", grant_type="client_credentials\""""),
					() -> Assertions.assertThat(output.getOut()).contains("""
							Status code 401: {"error":"invalid_client","error_description":"Invalid client or Invalid c\
							lient credentials"}"""),
					() -> Assertions.assertThat(output.getOut()).contains("""
							An empty access token will be defined since a new access token can not be obtained for clie\
							nt '%s' pointing to %s""".formatted(CLIENT_ID, tokenUri))
			);
		}
	}
}
