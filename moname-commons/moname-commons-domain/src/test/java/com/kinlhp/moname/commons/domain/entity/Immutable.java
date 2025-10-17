package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;

import jakarta.annotation.Nonnull;

public final class Immutable extends AbstractIdentifiable<Immutable, Integer> implements Cloneable {

	@Serial
	private static final long serialVersionUID = 4546707788881649623L;

	@Nonnull
	final String property = "value";

	public Immutable(@Nonnull final Integer identity) {
		super(identity);
	}

	/**
	 * @see Object#clone() clone
	 */
	@Nonnull
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO: Implement deep structure clone
		return super.clone();
	}
}
