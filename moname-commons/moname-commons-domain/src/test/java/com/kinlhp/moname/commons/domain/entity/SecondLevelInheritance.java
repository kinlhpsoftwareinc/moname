package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;

final class SecondLevelInheritance extends FirstLevelInheritance {

	@Serial
	private static final long serialVersionUID = 6316453876510160649L;

	SecondLevelInheritance(final Integer identity) {
		super(identity);
	}
}
