package com.kinlhp.moname.commons.test;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.MultipleFailuresError;

/**
 * Bean Validation (JSR 380) constraint validator.
 */
public final class Jsr380ConstraintValidator {

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

	/**
	 * Validate bean constraints and assert that all violations are as expected.
	 *
	 * @param bean               Constrained bean.
	 * @param expectedViolations Expected violations.
	 * @param <T>                Constrained bean type.
	 * @param <S>                Expected violations array type.
	 * @throws MultipleFailuresError All aggregated exceptions will be added as suppressed exceptions to the
	 *                               MultipleFailuresError.
	 */
	public static <T, S> void validate(final T bean, final S[] expectedViolations) {
		final var actualViolations = VALIDATOR.validate(bean);
		assertViolations(actualViolations, expectedViolations);
	}

	/**
	 * Assert that all violations are as expected.
	 *
	 * @param actual   Actual bean violations.
	 * @param expected Expected violations.
	 * @param <T>      Constrained bean type.
	 * @param <S>      Expected violations array type.
	 * @throws MultipleFailuresError All aggregated exceptions will be added as suppressed exceptions to the
	 *                               MultipleFailuresError.
	 */
	public static <T, S> void assertViolations(final Collection<ConstraintViolation<T>> actual, final S[] expected)
		throws MultipleFailuresError {
		Assertions.assertAll("violations", () -> Assertions.assertNotNull(actual),
			() -> MatcherAssert.assertThat(actual, Matchers.hasSize(expected.length)));
		final var violations = actual.stream().map(v -> v.getPropertyPath() + ": " + v.getMessage())
			.collect(Collectors.toUnmodifiableSet());

		Assertions.assertAll("violations", () -> MatcherAssert.assertThat(violations,
			Matchers.containsInAnyOrder(expected)));
	}

}
