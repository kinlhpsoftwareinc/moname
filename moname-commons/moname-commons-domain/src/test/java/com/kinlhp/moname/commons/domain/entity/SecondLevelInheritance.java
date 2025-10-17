package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;

import jakarta.annotation.Nonnull;

final class SecondLevelInheritance extends FirstLevelInheritance {

	@Serial
	private static final long serialVersionUID = 6316453876510160649L;

	SecondLevelInheritance(@Nonnull final Integer identity) {
		super(identity);
	}
}
