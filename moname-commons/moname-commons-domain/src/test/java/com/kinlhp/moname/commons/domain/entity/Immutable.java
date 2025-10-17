package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;

public final class Immutable extends AbstractIdentifiable<Immutable, Integer> implements Cloneable {

	@Serial
	private static final long serialVersionUID = 4546707788881649623L;

	final String property = "value";

	public Immutable(final Integer identity) {
		super(identity);
	}

	/**
	 * @see Object#clone() clone
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO: Implement deep structure clone
		return super.clone();
	}
}
