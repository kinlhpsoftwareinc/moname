package com.kinlhp.moname.commons.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kinlhp.moname.commons.test.validation.Jsr380ConstraintValidator;

/**
 * Constraint violation tests.
 */
class ConstraintViolationTests {

	private static final String[] MAX_CONSTRAINTS = {"bar: must be less than or equal to 0"};
	private static final String[] NOT_BLANK_CONSTRAINTS = {"foo: must not be blank"};
	private static final String[] NOT_NULL_CONSTRAINTS = {"bar: must not be null", "foo: must not be blank"};
	private static final String[] POSITIVE_OR_ZERO_CONSTRAINTS = {"bar: must be greater than or equal to 0"};
	private static final String[] SIZE_CONSTRAINTS = {"foo: size must be between 0 and 1"};

	@DisplayName(value = "Without violations.")
	@Test
	final void shouldNotViolateConstraints() {
		final var bean = ConstraintViolationFittings.withoutViolations();
		Jsr380ConstraintValidator.validate(bean, new Object[]{});
	}

	@DisplayName(value = "@Max constraint violations.")
	@Test
	final void shouldViolateMaxConstraints() {
		final var bean = ConstraintViolationFittings.maxViolations();
		Jsr380ConstraintValidator.validate(bean, MAX_CONSTRAINTS);
	}

	@DisplayName(value = "@NotBlank constraint violations.")
	@Test
	final void shouldViolateNotBlankConstraints() {
		final var bean = ConstraintViolationFittings.notBlankViolations();
		Jsr380ConstraintValidator.validate(bean, NOT_BLANK_CONSTRAINTS);
	}

	@DisplayName(value = "@NotNull constraint violations.")
	@Test
	final void shouldViolateNotNullConstraints() {
		final var bean = ConstraintViolationFittings.notNullViolations();
		Jsr380ConstraintValidator.validate(bean, NOT_NULL_CONSTRAINTS);
	}

	@DisplayName(value = "@PositiveOrZero constraint violations.")
	@Test
	final void shouldViolatePositiveOrZeroConstraints() {
		final var bean = ConstraintViolationFittings.positiveOrZeroViolations();
		Jsr380ConstraintValidator.validate(bean, POSITIVE_OR_ZERO_CONSTRAINTS);
	}

	@DisplayName(value = "@Size constraint violations.")
	@Test
	final void shouldViolateSizeConstraints() {
		final var bean = ConstraintViolationFittings.sizeViolations();
		Jsr380ConstraintValidator.validate(bean, SIZE_CONSTRAINTS);
	}

}
