package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;

import jakarta.annotation.Nonnull;

class FirstLevelInheritance extends Parent {

	@Serial
	private static final long serialVersionUID = 5061036059100597801L;

	FirstLevelInheritance(@Nonnull final Integer identity) {
		super(identity);
	}
}
