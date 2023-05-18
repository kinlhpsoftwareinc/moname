package com.kinlhp.moname.commons.domain.entity;

import jakarta.annotation.Nonnull;

@SuppressWarnings(value = "serial")
public final class Immutable extends AbstractIdentifiable<Immutable, Integer> implements Cloneable {

	@Nonnull
	final String property = "value";

	public Immutable(final @Nonnull Integer identity) {
		super(identity);
	}

	@Nonnull
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
