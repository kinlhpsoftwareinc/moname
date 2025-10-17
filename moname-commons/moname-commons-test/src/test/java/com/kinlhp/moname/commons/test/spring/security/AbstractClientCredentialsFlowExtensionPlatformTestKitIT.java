package com.kinlhp.moname.commons.test.spring.security;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Event;
import org.junit.platform.testkit.engine.Events;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Nonnull;
import java.lang.reflect.Method;

import com.kinlhp.moname.commons.test.spring.jpa.AbstractDataJpaTestSliceIT;
import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.PropertiesKeycloakConnectionDetails;

import static com.kinlhp.moname.commons.test.spring.security.AbstractClientCredentialsFlowExtensionPlatformTestKitIT.TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY;

/**
 * <strong>NOTE:</strong> Inheriting from {@link AbstractDataJpaTestSliceIT AbstractDataJpaTestSliceIT} is mandatory
 * because optimized Keycloak container requires a MySQL instance having {@literal mysql} as hostname on the same bridge
 * network
 *
 * @see <a href="README#filtering-tests">Flaky with junit-platform-launcher-api</a>
 */
@DataJpaTest
@Tag(TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
abstract class AbstractClientCredentialsFlowExtensionPlatformTestKitIT extends AbstractDataJpaTestSliceIT {

	@Nonnull
	static final String TWICE_EXECUTION_WORKAROUND_DISABLED_REASON =
			"Fix twice execution: https://stackoverflow.com/q/72911486";

	@Nonnull
	static final String TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY = "junit-platform-test-kit";

	@Nonnull
	static final String TWICE_EXECUTION_WORKAROUND_PROPERTY_VALUE = "enabled";

	@Nonnull
	static final String FAILED_MESSAGE = TWICE_EXECUTION_WORKAROUND_PROPERTY_KEY + " failed";

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

	protected abstract void assertTestEvents(@Nonnull final Events events);

	@MethodSource("factoryMethod")
	@ParameterizedTest
	final void assertFor(@Nonnull final Class<? extends ClassToBeTested> classToBeTested) throws NoSuchMethodException {
		@Nonnull final var methodToBeTested = classToBeTested.getDeclaredMethod("methodToBeTested",
				CapturedOutput.class, PropertiesKeycloakConnectionDetails.class);
		@Nonnull final var testEvents = executeAndGetTestEvents(classToBeTested, methodToBeTested);
		testEvents.failed().list().forEach(this::printStackTrace);
		assertTestEvents(testEvents);
	}

	/**
	 * @see <a href="https://docs.junit.org/current/user-guide/#testkit">JUnit Platform Test Kit</a>
	 */
	@Nonnull
	private Events executeAndGetTestEvents(@Nonnull final Class<? extends ClassToBeTested> classToBeTested,
			@Nonnull final Method methodToBeTested) {
		return EngineTestKit.engine(JupiterEngineDescriptor.ENGINE_ID)
				.selectors(DiscoverySelectors.selectMethod(classToBeTested, methodToBeTested))
				// TODO: Fix twice execution:
				//  - https://stackoverflow.com/q/72911486
				.execute()
				.testEvents();
	}

	private void printStackTrace(@Nonnull final Event event) {
		event.getPayload(TestExecutionResult.class)
				.flatMap(TestExecutionResult::getThrowable)
				.ifPresent(Throwable::printStackTrace);
	}

	@ExtendWith(OutputCaptureExtension.class)
	@WebMvcTest
	interface ClassToBeTested {

		void methodToBeTested(@Nonnull final CapturedOutput output,
				@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails);
	}
}
