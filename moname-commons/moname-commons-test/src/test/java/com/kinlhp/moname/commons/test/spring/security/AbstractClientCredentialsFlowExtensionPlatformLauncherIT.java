package com.kinlhp.moname.commons.test.spring.security;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Nonnull;
import java.lang.reflect.Method;

import com.kinlhp.moname.commons.test.spring.jpa.AbstractDataJpaTestSliceIT;
import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.PropertiesKeycloakConnectionDetails;

/**
 * <strong>NOTE:</strong> Inheriting from {@link AbstractDataJpaTestSliceIT AbstractDataJpaTestSliceIT} is mandatory
 * because optimized Keycloak container requires a MySQL instance having {@literal mysql} as hostname on the same bridge
 * network
 */
@Transactional(propagation = Propagation.NOT_SUPPORTED)
abstract class AbstractClientCredentialsFlowExtensionPlatformLauncherIT extends AbstractDataJpaTestSliceIT {

	@Nonnull
	static final String TWICE_EXECUTION_WORKAROUND_DISABLED_REASON =
			"Fix twice execution: https://stackoverflow.com/q/72911486";

	@Nonnull
	static final String TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY = "junit-platform-launcher-api";

	@Nonnull
	static final String TWICE_EXECUTION_WORKAROUND_PROPERTY_VALUE = "enabled";

	@Nonnull
	static final String FAILURE_MESSAGE = TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY + " failure";

	@BeforeAll
	static void beforeAll() {
		// TODO: Fix twice execution:
		//  - https://stackoverflow.com/q/72911486
		System.setProperty(
				TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY,
				TWICE_EXECUTION_WORKAROUND_PROPERTY_VALUE
		);
	}

	@AfterAll
	static void afterAll() {
		// TODO: Fix twice execution:
		//  - https://stackoverflow.com/q/72911486
		System.clearProperty(TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY);
	}

	protected abstract void assertTestExecutionSummary(@Nonnull final TestExecutionSummary summary);

	@MethodSource("factoryMethod")
	@ParameterizedTest
	final void assertFor(@Nonnull final Class<? extends ClassToBeTested> classToBeTested) throws NoSuchMethodException {
		@Nonnull final var methodToBeTested = classToBeTested.getDeclaredMethod("methodToBeTested",
				CapturedOutput.class, PropertiesKeycloakConnectionDetails.class);
		@Nonnull final var testExecutionSummary = executeAndGetSummary(classToBeTested, methodToBeTested);
		testExecutionSummary.getFailures().forEach(this::printStackTrace);
		assertTestExecutionSummary(testExecutionSummary);
	}

	/**
	 * @see <a href="https://docs.junit.org/current/user-guide/#launcher-api-execution">
	 * JUnit Platform Launcher API / Executing Tests
	 * </a>
	 */
	@Nonnull
	private TestExecutionSummary executeAndGetSummary(@Nonnull final Class<? extends ClassToBeTested> classToBeTested,
			@Nonnull final Method methodToBeTested) {
		@Nonnull final var request = LauncherDiscoveryRequestBuilder.request()
				.selectors(DiscoverySelectors.selectMethod(classToBeTested, methodToBeTested))
				.build();
		@Nonnull final var listener = new SummaryGeneratingListener();
		// TODO: Fix twice execution:
		//  - https://stackoverflow.com/q/72911486
		LauncherFactory.create().execute(request, listener);
		return listener.getSummary();
	}

	private void printStackTrace(@Nonnull final Failure failure) {
		//noinspection CallToPrintStackTrace
		failure.getException().printStackTrace();
	}

	@ExtendWith(OutputCaptureExtension.class)
	@WebMvcTest
	interface ClassToBeTested {

		void methodToBeTested(@Nonnull final CapturedOutput output,
				@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails);
	}
}
