package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;

import jakarta.annotation.Nonnull;

final class Uncloneable extends AbstractIdentifiable<Uncloneable, Integer> {

	@Serial
	private static final long serialVersionUID = -4082778002335542326L;

	Uncloneable(@Nonnull final Integer identity) {
		super(identity);
	}
}
