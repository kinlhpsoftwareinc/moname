package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;

final class Uncloneable extends AbstractIdentifiable<Uncloneable, Integer> {

	@Serial
	private static final long serialVersionUID = -4082778002335542326L;

	Uncloneable(final Integer identity) {
		super(identity);
	}
}
