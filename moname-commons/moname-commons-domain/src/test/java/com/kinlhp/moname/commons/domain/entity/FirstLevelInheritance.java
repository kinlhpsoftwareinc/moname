package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;

class FirstLevelInheritance extends Parent {

	@Serial
	private static final long serialVersionUID = 5061036059100597801L;

	FirstLevelInheritance(final Integer identity) {
		super(identity);
	}
}
