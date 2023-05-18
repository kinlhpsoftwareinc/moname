package com.kinlhp.moname.commons.domain.entity;

import jakarta.annotation.Nonnull;

@SuppressWarnings(value = "serial")
class Parent extends AbstractIdentifiable<Parent, Integer> {

	Parent(final @Nonnull Integer identity) {
		super(identity);
	}
}
