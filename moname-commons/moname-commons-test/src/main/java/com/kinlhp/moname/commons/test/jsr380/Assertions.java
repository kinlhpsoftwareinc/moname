package com.kinlhp.moname.commons.test.jsr380;

import java.util.Collection;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.hamcrest.MatcherAssert;
import org.opentest4j.MultipleFailuresError;

/**
 * Assertions for Bean Validation (JSR 380).
 */
public interface Assertions {

	/**
	 * Validate bean constraints and assert that all violations are as expected.
	 *
	 * @param bean Constrained bean.
	 * @param <T>  Constrained bean type.
	 * @param <E>  Expected violations array type.
	 * @throws MultipleFailuresError All aggregated exceptions will be added as suppressed exceptions to the
	 *                               MultipleFailuresError.
	 */
	@Nonnull
	static <T, E> Matchers<E> assertThat(@Nonnull final T bean) throws MultipleFailuresError {
		return expectedViolations -> assertViolations(bean, expectedViolations);
	}

	private static <T, E> void assertViolations(@Nonnull final T bean, @Nonnull final E[] expectedViolations)
			throws MultipleFailuresError {
		@Nonnull final var actualViolations = ConstraintValidator.getValidator().validate(bean);
		assertViolations(actualViolations, expectedViolations);
	}

	private static <T, E> void assertViolations(@Nonnull final Collection<ConstraintViolation<T>> actual,
			@Nonnull final E[] expected) throws MultipleFailuresError {
		org.junit.jupiter.api.Assertions.assertAll("violations count",
				() -> org.junit.jupiter.api.Assertions.assertNotNull(actual),
				() -> MatcherAssert.assertThat(actual, org.hamcrest.Matchers.hasSize(expected.length))
		);
		@Nonnull final var violations = actual.stream()
				.map(v -> v.getPropertyPath() + ": " + v.getMessage())
				.collect(Collectors.toUnmodifiableSet());
		org.junit.jupiter.api.Assertions.assertAll("constraint violations",
				() -> MatcherAssert.assertThat(violations, org.hamcrest.Matchers.containsInAnyOrder(expected))
		);
	}

	@FunctionalInterface
	interface Matchers<E> {

		void violates(@Nonnull E[] expectedViolations);
	}

	final class ConstraintValidator {

		@SuppressWarnings("java:S3077")
		private static volatile Validator validator = null;

		@Nonnull
		public static Validator getValidator() {
			if (validator == null) {
				synchronized (Validator.class) {
					if (validator == null) {
						try (@Nonnull final var validatorFactory = Validation.buildDefaultValidatorFactory()) {
							validator = validatorFactory.getValidator();
						}
					}
				}
			}
			return validator;
		}

		private ConstraintValidator() {
		}
	}
}
