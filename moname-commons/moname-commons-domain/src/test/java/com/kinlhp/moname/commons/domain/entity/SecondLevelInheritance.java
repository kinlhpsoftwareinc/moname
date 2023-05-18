package com.kinlhp.moname.commons.domain.entity;

import jakarta.annotation.Nonnull;

@SuppressWarnings(value = "serial")
class SecondLevelInheritance extends FirstLevelInheritance {

	SecondLevelInheritance(final @Nonnull Integer identity) {
		super(identity);
	}
}
