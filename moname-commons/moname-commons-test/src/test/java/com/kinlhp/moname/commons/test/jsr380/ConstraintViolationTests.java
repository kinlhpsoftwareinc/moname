package com.kinlhp.moname.commons.test.jsr380;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Constraint violation tests.
 */
// TODO: Remove Lombok.
@NoArgsConstructor(onConstructor_ = {@Nonnull})
final class ConstraintViolationTests {

	@Nonnull
	private static final String[] MAX_CONSTRAINTS = {"bar: must be less than or equal to 0"};
	@Nonnull
	private static final String[] NOT_BLANK_CONSTRAINTS = {"foo: must not be blank"};
	@Nonnull
	private static final String[] NOT_NULL_CONSTRAINTS = {"bar: must not be null", "foo: must not be blank"};
	@Nonnull
	private static final String[] POSITIVE_OR_ZERO_CONSTRAINTS = {"bar: must be greater than or equal to 0"};
	@Nonnull
	private static final String[] SIZE_CONSTRAINTS = {"foo: size must be between 0 and 1"};

	@DisplayName(value = "Without violations.")
	@Test
    void shouldNotViolateConstraints() {
		@Nonnull final var bean = ConstraintViolationFittings.withoutViolations();
		Assertions.assertThat(bean).containsInAnyOrder(new Object[]{});
	}

	@DisplayName(value = "@Max constraint violations.")
	@Test
    void shouldViolateMaxConstraints() {
		@Nonnull final var bean = ConstraintViolationFittings.maxViolations();
		Assertions.assertThat(bean).containsInAnyOrder(MAX_CONSTRAINTS);
	}

	@DisplayName(value = "@NotBlank constraint violations.")
	@Test
    void shouldViolateNotBlankConstraints() {
		@Nonnull final var bean = ConstraintViolationFittings.notBlankViolations();
		Assertions.assertThat(bean).containsInAnyOrder(NOT_BLANK_CONSTRAINTS);
	}

	@DisplayName(value = "@NotNull constraint violations.")
	@Test
    void shouldViolateNotNullConstraints() {
		@Nonnull final var bean = ConstraintViolationFittings.notNullViolations();
		Assertions.assertThat(bean).containsInAnyOrder(NOT_NULL_CONSTRAINTS);
	}

	@DisplayName(value = "@PositiveOrZero constraint violations.")
	@Test
    void shouldViolatePositiveOrZeroConstraints() {
		@Nonnull final var bean = ConstraintViolationFittings.positiveOrZeroViolations();
		Assertions.assertThat(bean).containsInAnyOrder(POSITIVE_OR_ZERO_CONSTRAINTS);
	}

	@DisplayName(value = "@Size constraint violations.")
	@Test
    void shouldViolateSizeConstraints() {
		@Nonnull final var bean = ConstraintViolationFittings.sizeViolations();
		Assertions.assertThat(bean).containsInAnyOrder(SIZE_CONSTRAINTS);
	}
}
