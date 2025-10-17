package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;

import jakarta.annotation.Nonnull;

class Parent extends AbstractIdentifiable<Parent, Integer> {

	@Serial
	private static final long serialVersionUID = 2913276736907117032L;

	Parent(@Nonnull final Integer identity) {
		super(identity);
	}
}
