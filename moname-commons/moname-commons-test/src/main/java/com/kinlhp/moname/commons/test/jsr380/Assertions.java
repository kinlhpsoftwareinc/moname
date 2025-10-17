package com.kinlhp.moname.commons.test.jsr380;

import org.hamcrest.MatcherAssert;
import org.opentest4j.MultipleFailuresError;

import jakarta.annotation.Nonnull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Collection;
import java.util.stream.Collectors;

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

		/**
		 * Safe private lock-splitting approach.
		 */
		@Nonnull
		private static final ValidatorPrivateLock VALIDATOR_PRIVATE_LOCK = new ValidatorPrivateLock();

		@SuppressWarnings("java:S3077")
		private static volatile Validator validator = null;

		@Nonnull
		public static Validator getValidator() {
			// The local variable `readingReducer` is not merely cosmetic. It reduces the number of reads of the
			// volatile field from two or three to one on the fast path. It is a micro-optimization, but it comes for
			// free.
			var readingReducer = validator;
			if (readingReducer == null) {
				synchronized (VALIDATOR_PRIVATE_LOCK) {
					readingReducer = validator;
					if (readingReducer == null) {
						try (@Nonnull final var validatorFactory = Validation.buildDefaultValidatorFactory()) {
							readingReducer = validatorFactory.getValidator();
							validator = readingReducer;
						} catch (@Nonnull final Exception exception) {
							validator = null;
							throw exception;
						}
					}
				}
			}
			return readingReducer;
		}

		private ConstraintValidator() {
		}

		/**
		 * Safe private lock-splitting approach.
		 *
		 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">
		 * The "Double-Checked Locking is Broken" Declaration
		 * </a>
		 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html">JSR 133 (Java Memory Model) FAQ</a>
		 * @see <a href="https://docs.oracle.com/javase/specs/jls/se24/html/jls-17.html">Chapter 17. Threads and Locks</a>
		 * @see <a href="https://jcp.org/en/jsr/detail?id=133">
		 * JSR 133: JavaTM Memory Model and Thread Specification Revision
		 * </a>
		 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel">The Java Memory Model</a>
		 */
		@SuppressWarnings("java:S2094")
		private static final class ValidatorPrivateLock {
		}
	}
}
