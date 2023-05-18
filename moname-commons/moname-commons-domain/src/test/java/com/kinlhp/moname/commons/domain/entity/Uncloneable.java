package com.kinlhp.moname.commons.domain.entity;

import jakarta.annotation.Nonnull;

@SuppressWarnings(value = "serial")
final class Uncloneable extends AbstractIdentifiable<Uncloneable, Integer> {

	Uncloneable(@Nonnull final Integer identity) {
		super(identity);
	}
}
