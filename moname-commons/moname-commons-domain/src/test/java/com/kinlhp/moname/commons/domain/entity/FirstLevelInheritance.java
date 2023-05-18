package com.kinlhp.moname.commons.domain.entity;

import jakarta.annotation.Nonnull;

@SuppressWarnings(value = "serial")
class FirstLevelInheritance extends Parent {

	FirstLevelInheritance(final @Nonnull Integer identity) {
		super(identity);
	}
}
