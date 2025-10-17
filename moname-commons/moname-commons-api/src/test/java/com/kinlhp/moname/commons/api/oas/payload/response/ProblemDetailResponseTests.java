package com.kinlhp.moname.commons.api.oas.payload.response;

import jakarta.annotation.Nonnull;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kinlhp.moname.commons.test.jsr380.Assertions;

/**
 * Constraint violation tests.
 */
class ProblemDetailResponseTests {

	@Nonnull
	private static final String[] MAX_CONSTRAINTS = {"status: must be less than or equal to 511"};

	@Nonnull
	private static final String[] MIN_CONSTRAINTS = {"status: must be greater than or equal to 400"};

	@Nonnull
	private static final String[] NOT_NULL_CONSTRAINTS = {"detail: must not be null", "title: must not be null"};

	@Nonnull
	private static final String[] SIZE_CONSTRAINTS = {"detail: size must be between 1 and 128", "title: size must be between 4 and 31"};

	@DisplayName("Without violations")
	@Test
	final void shouldNotViolateConstraints() {
		@Nonnull final var bean = ConstraintViolationFittings.withoutViolations();
		Assertions.assertThat(bean).violates(new Object[]{});
	}

	@DisplayName("@Max constraint violations")
	@Test
	final void shouldViolateMaxConstraints() {
		@Nonnull final var bean = ConstraintViolationFittings.maxViolations();
		Assertions.assertThat(bean).violates(MAX_CONSTRAINTS);
	}

	@DisplayName("@Min constraint violations")
	@Test
	final void shouldViolateMinConstraints() {
		@Nonnull final var bean = ConstraintViolationFittings.minViolations();
		Assertions.assertThat(bean).violates(MIN_CONSTRAINTS);
	}

	@Disabled("The readOnly property of the OpenAPI Specification results in not adding the @NotNull annotation ([[Spring-java] required readonly attribute generate not null field](https://github.com/OpenAPITools/openapi-generator/issues/5026))")
	@DisplayName("@NotNull constraint violations")
	@Test
	final void shouldViolateNotNullConstraints() {
		@Nonnull final var bean = ConstraintViolationFittings.notNullViolations();
		Assertions.assertThat(bean).violates(NOT_NULL_CONSTRAINTS);
	}

	@DisplayName("@Size constraint violations")
	@Test
	final void shouldViolateSizeConstraints() {
		@Nonnull final var bean = ConstraintViolationFittings.sizeViolations();
		Assertions.assertThat(bean).violates(SIZE_CONSTRAINTS);
	}
}
