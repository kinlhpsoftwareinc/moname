package com.kinlhp.moname.commons.test.jsr380;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;

/**
 * Fittings for the bean tests.
 */
// TODO: Remove Lombok.
@NoArgsConstructor(onConstructor_ = {@Nonnull})
final class ConstraintViolationFittings {

	@Nonnull
	static Bean maxViolations() {
		return Bean.builder().bar(1L).foo("1").build();
	}

	@Nonnull
	static Bean notBlankViolations() {
		return Bean.builder().bar(0L).foo(" ").build();
	}

	@Nonnull
	static Bean notNullViolations() {
		return Bean.builder().build();
	}

	@Nonnull
	static Bean positiveOrZeroViolations() {
		return Bean.builder().bar(-1L).foo("1").build();
	}

	@Nonnull
	static Bean sizeViolations() {
		return Bean.builder().bar(0L).foo("two").build();
	}

	@Nonnull
	static Bean withoutViolations() {
		return Bean.builder().bar(0L).foo("1").build();
	}
}
