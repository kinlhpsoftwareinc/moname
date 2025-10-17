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

final class ClientCredentialsFlowExtensionPlatformLauncherFailureIT
		extends AbstractClientCredentialsFlowExtensionPlatformLauncherIT {

	@Nonnull
	private static Stream<Class<? extends ClassToBeTested>> factoryMethod() {
		return Stream.of(ClientCredentialsFailureIT.class);
	}

	@Override
	protected void assertTestExecutionSummary(@Nonnull final TestExecutionSummary summary) {
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(summary.getTestsSucceededCount()).isZero(),
				() -> Assertions.assertThat(summary.getFailures())
						.allMatch(failure -> failure.getException().getMessage().contains(FAILURE_MESSAGE))
		);
	}

	@EnabledIfSystemProperty(
			disabledReason = TWICE_EXECUTION_WORKAROUND_DISABLED_REASON,
			matches = TWICE_EXECUTION_WORKAROUND_PROPERTY_VALUE,
			named = TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY
	) // TODO: Fix twice execution: https://stackoverflow.com/q/72911486
	@ImportAutoConfiguration({KeycloakAutoConfiguration.class, SslAutoConfiguration.class})
	static final class ClientCredentialsFailureIT implements ClassToBeTested {

		private static final String CLIENT_ID = "failure@" + TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY;

		@ClientCredentialsFlow(
				clientId = CLIENT_ID,
				clientSecret = "failure"
		)
		@Override
		@Test
		public void methodToBeTested(@Nonnull final CapturedOutput output,
				@Autowired @Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
			@Nonnull final var tokenUri = keycloakConnectionDetails.getTokenUri();
			Assertions.assertThat(output.getOut()).contains("The client '%s' is not yet associated with an access token"
					.formatted(CLIENT_ID));
			Assertions.assertThat(output.getOut()).contains(
					"Requesting a new access token for client '%s' pointing to %s".formatted(CLIENT_ID, tokenUri));
			Assertions.assertThat(output.getOut()).doesNotContain("Bearer token successfully obtained and expires at ");
			Assertions.assertThat(output.getOut()).contains("""
					type="CLIENT_LOGIN_ERROR", realmId="b5ad5326-5378-421f-a883-4dc292221c95", realmName="moname", clie\
					ntId="%s", userId="null", ipAddress=\"""".formatted(CLIENT_ID));
			Assertions.assertThat(output.getOut()).contains("""
					", error="client_not_found", grant_type="client_credentials\"""");
			Assertions.assertThat(output.getOut()).contains("""
					Status code 401: {"error":"invalid_client","error_description":"Invalid client or Invalid client cr\
					edentials"}""");
			Assertions.assertThat(output.getOut()).contains("""
					An empty access token will be defined since a new access token can not be obtained for client '%s' \
					pointing to %s""".formatted(CLIENT_ID, tokenUri));
			Assertions.fail(FAILURE_MESSAGE);
		}
	}
}
