package com.kinlhp.moname.commons.api.oas.payload.response;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kinlhp.moname.commons.test.jsr380.Assertions;

/**
 * Constraint violation tests.
 */
class ProblemDetailResponseTests {

	private static final String[] MAX_CONSTRAINTS = {"status: must be less than or equal to 511"};
	private static final String[] MIN_CONSTRAINTS = {"status: must be greater than or equal to 400"};
	private static final String[] NOT_NULL_CONSTRAINTS = {"detail: must not be null", "title: must not be null"};
	private static final String[] SIZE_CONSTRAINTS = {"detail: size must be between 1 and 128", "title: size must be between 4 and 31"};

	@DisplayName(value = "Without violations.")
	@Test
	final void shouldNotViolateConstraints() {
		final var bean = ConstraintViolationFittings.withoutViolations();
		Assertions.assertThat(bean).containsInAnyOrder(new Object[]{});
	}

	@DisplayName(value = "@Max constraint violations.")
	@Test
	final void shouldViolateMaxConstraints() {
		final var bean = ConstraintViolationFittings.maxViolations();
		Assertions.assertThat(bean).containsInAnyOrder(MAX_CONSTRAINTS);
	}

	@DisplayName(value = "@Min constraint violations.")
	@Test
	final void shouldViolateMinConstraints() {
		final var bean = ConstraintViolationFittings.minViolations();
		Assertions.assertThat(bean).containsInAnyOrder(MIN_CONSTRAINTS);
	}

	@Disabled(value = "The readOnly property of the OpenAPI Specification results in not adding the @NotNull annotation ([Spring-java] required readonly attribute generate not null field](https://github.com/OpenAPITools/openapi-generator/issues/5026)).")
	@DisplayName(value = "@NotNull constraint violations.")
	@Test
	final void shouldViolateNotNullConstraints() {
		final var bean = ConstraintViolationFittings.notNullViolations();
		Assertions.assertThat(bean).containsInAnyOrder(NOT_NULL_CONSTRAINTS);
	}

	@DisplayName(value = "@Size constraint violations.")
	@Test
	final void shouldViolateSizeConstraints() {
		final var bean = ConstraintViolationFittings.sizeViolations();
		Assertions.assertThat(bean).containsInAnyOrder(SIZE_CONSTRAINTS);
	}

}
