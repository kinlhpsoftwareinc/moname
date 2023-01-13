package com.kinlhp.moname.commons.api.oas.payload.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kinlhp.moname.commons.test.validation.Jsr380ConstraintValidator;

/**
 * Constraint violation tests.
 */
class GetEntitiesDefaultResponseDTOTests {

	private static final String[] MAX_CONSTRAINTS = { "code: must be less than or equal to 599" };
	private static final String[] MIN_CONSTRAINTS = { "code: must be greater than or equal to 500" };
	private static final String[] NOT_NULL_CONSTRAINTS = { "code: must not be null", "description: must not be null" };
	private static final String[] SIZE_CONSTRAINTS = { "description: size must be between 11 and 31" };

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

	@DisplayName(value = "@Min constraint violations.")
	@Test
	final void shouldViolateMinConstraints() {
		final var bean = ConstraintViolationFittings.minViolations();
		Jsr380ConstraintValidator.validate(bean, MIN_CONSTRAINTS);
	}

	@DisplayName(value = "@NotNull constraint violations.")
	@Test
	final void shouldViolateNotNullConstraints() {
		final var bean = ConstraintViolationFittings.notNullViolations();
		Jsr380ConstraintValidator.validate(bean, NOT_NULL_CONSTRAINTS);
	}

	@DisplayName(value = "@Size constraint violations.")
	@Test
	final void shouldViolateSizeConstraints() {
		final var bean = ConstraintViolationFittings.sizeViolations();
		Jsr380ConstraintValidator.validate(bean, SIZE_CONSTRAINTS);
	}

}
