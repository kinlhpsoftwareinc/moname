package com.kinlhp.moname.commons.test;

/**
 * Fittings for the bean tests.
 */
final class ConstraintViolationFittings {

	static Bean maxViolations() {
		return Bean.builder().bar(1L).foo("1").build();
	}

	static Bean notBlankViolations() {
		return Bean.builder().bar(0L).foo(" ").build();
	}

	static Bean notNullViolations() {
		return Bean.builder().build();
	}

	static Bean positiveOrZeroViolations() {
		return Bean.builder().bar(-1L).foo("1").build();
	}

	static Bean sizeViolations() {
		return Bean.builder().bar(0L).foo("two").build();
	}

	static Bean withoutViolations() {
		return Bean.builder().bar(0L).foo("1").build();
	}

}
