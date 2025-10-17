package com.kinlhp.moname.commons.test.spring.security;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Event;
import org.junit.platform.testkit.engine.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.event;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.EventConditions.test;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

/**
 * Behaviors observable only as JUnit Platform outcomes.
 * <p>
 * Fixtures are {@link Disabled @Disabled} so that they never run on their own (e.g. when an IDE scans the package), and
 * are executed through {@link EngineTestKit EngineTestKit} with
 * {@link org.junit.jupiter.engine.extension.DisabledCondition DisabledCondition} deactivated through the
 * {@code junit.jupiter.conditions.deactivate} configuration parameter.
 *
 * @see <a href="https://docs.junit.org/current/extensions/conditional-test-execution#deactivation">
 * Conditional Test Execution / Deactivating Conditions
 * </a>
 */
@DisplayName("ClientCredentialsFlowExtension on the JUnit Platform")
class ClientCredentialsFlowExtensionEngineTestKitTest {

	static final String ENGINE_TEST_KIT_ONLY = "Fixture executed only through EngineTestKit";

	private static final String CONDITIONS_DEACTIVATE = "junit.jupiter.conditions.deactivate";

	private static final String DISABLED_CONDITION = "org.junit.*DisabledCondition";

	private static final String METHOD_TO_BE_TESTED = "methodToBeTested";

	@DisplayName("fails to resolve a MockMvc outside a web application context")
	@Test
	final void failsOutsideWebApplicationContext() {
		assertSingleParameterResolutionFailure(NonWebApplicationContextFixture.class,
				"""
						A %s can only be resolved within a web application context (e.g. @%s or @%s with a mock web env\
						ironment)"""
						.formatted(
								MockMvc.class.getSimpleName(),
								WebMvcTest.class.getSimpleName(),
								SpringBootTest.class.getSimpleName()
						));
	}

	@DisplayName("rejects an annotated parameter of an autowirable constructor as ambiguous")
	@Test
	final void rejectsAnnotatedParameterOfAutowirableConstructorAsAmbiguous() {
		assertSingleParameterResolutionFailure(AutowiredConstructorFixture.class,
				"Discovered multiple competing %ss for parameter [%s arg0] in constructor"
						.formatted(ParameterResolver.class.getSimpleName(), MockMvc.class.getName()),
				SpringExtension.class.getName(),
				ClientCredentialsFlowExtension.class.getName());
	}

	@DisplayName("does not resolve an unannotated constructor parameter from the test method annotation")
	@Test
	final void doesNotResolveUnannotatedConstructorParameter() {
		assertSingleParameterResolutionFailure(UnannotatedConstructorParameterFixture.class,
				"No %s registered for parameter [%s arg0] in constructor"
						.formatted(ParameterResolver.class.getSimpleName(), MockMvc.class.getName()));
	}

	@DisplayName("rejects a parameter both annotated and @Autowired as ambiguous")
	@Test
	final void rejectsAnnotatedAutowiredParameterAsAmbiguous() {
		assertSingleParameterResolutionFailure(AutowiredAnnotatedParameterFixture.class,
				"""
						Discovered multiple competing %ss for parameter [%s arg0] in method [final void %s.methodToBeTe\
						sted(%s)]: %s"""
						.formatted(
								ParameterResolver.class.getSimpleName(),
								MockMvc.class.getName(),
								AutowiredAnnotatedParameterFixture.class.getName(),
								MockMvc.class.getName(),
								SpringExtension.class.getName()
						),
				", %s".formatted(ClientCredentialsFlowExtension.class.getName()));
	}

	private static void assertSingleParameterResolutionFailure(final Class<?> fixtureClassToBeTested,
			final String... expectedMessageParts) {
		final var events = executeTests(fixtureClassToBeTested);
		events.failed().list().forEach(ClientCredentialsFlowExtensionEngineTestKitTest::printStackTrace);
		events
				.assertStatistics(statistics -> statistics.started(1L).failed(1L))
				.assertThatEvents()
				.haveExactly(1, event(
						test(METHOD_TO_BE_TESTED),
						finishedWithFailure(
								instanceOf(ParameterResolutionException.class),
								message(message -> Stream.of(expectedMessageParts).allMatch(message::contains)))));
	}

	private static Events executeTests(final Class<?> fixtureClassToBeTested) {
		return EngineTestKit.engine(JupiterEngineDescriptor.ENGINE_ID)
				.configurationParameter(CONDITIONS_DEACTIVATE, DISABLED_CONDITION)
				.selectors(selectClass(fixtureClassToBeTested))
				.execute()
				.testEvents();
	}

	private static void printStackTrace(final Event event) {
		event.getPayload(TestExecutionResult.class)
				.flatMap(TestExecutionResult::getThrowable)
				.ifPresent(Throwable::printStackTrace);
	}

	@Disabled(ENGINE_TEST_KIT_ONLY)
	@SpringJUnitConfig(EngineTestKitEmptyTestConfiguration.class)
	static class NonWebApplicationContextFixture {

		@ClientCredentialsFlow(clientId = "engine.non-web@moname")
		@Test
		final void methodToBeTested(final MockMvc unresolvedMockMvc) {
			// Never reached: the parameter cannot be resolved
		}
	}

	@Disabled(ENGINE_TEST_KIT_ONLY)
	@SpringJUnitConfig(EngineTestKitEmptyTestConfiguration.class)
	static class AutowiredConstructorFixture {

		@Autowired
		AutowiredConstructorFixture(
				@ClientCredentialsFlow(clientId = "engine.autowired-constructor@moname")
				final MockMvc multipleCompetingMockMvc) {
			// Never reached: SpringExtension (autowirable constructor) and ClientCredentialsFlowExtension
			// (@ClientCredentialsFlow) both support the parameter
		}

		@Test
		final void methodToBeTested() {
			// Never reached: the test instance cannot be created
		}
	}

	@Disabled(ENGINE_TEST_KIT_ONLY)
	@SpringJUnitConfig(EngineTestKitEmptyTestConfiguration.class)
	static class UnannotatedConstructorParameterFixture {

		UnannotatedConstructorParameterFixture(final MockMvc unresolvedMockMvc) {
			// Never reached: the test method annotation does not apply to constructor parameters
		}

		@ClientCredentialsFlow(clientId = "engine.unannotated-constructor@moname")
		@Test
		final void methodToBeTested() {
			// Never reached: the test instance cannot be created
		}
	}

	@Disabled(ENGINE_TEST_KIT_ONLY)
	@SpringJUnitConfig(EngineTestKitEmptyTestConfiguration.class)
	static class AutowiredAnnotatedParameterFixture {

		@Test
		final void methodToBeTested(
				@Autowired
				@ClientCredentialsFlow(clientId = "engine.autowired@moname")
				final MockMvc multipleCompetingMockMvc
		) {
			// Never reached: both SpringExtension (@Autowired) and ClientCredentialsFlowExtension
			// (@ClientCredentialsFlow) support the parameter
		}
	}

	@Configuration(proxyBeanMethods = false)
	static class EngineTestKitEmptyTestConfiguration {
	}
}
