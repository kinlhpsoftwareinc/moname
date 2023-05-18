package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.jpa.AbstractPersistable;

interface AbstractPersistableArchTests<T extends AbstractPersistable<PK>, PK extends Serializable> extends
	PersistableArchTests<T, PK> {

	default void assertAbstractPersistableArch(@Nonnull final Class<T> aClass) throws NoSuchMethodException {
		PersistableArchTests.super.assertPersistableArch(aClass);
	}

	void assertImplements(@Nonnull Class<T> aClass);

	void assertIsAnnotated(@Nonnull Class<T> aClass);

	void assertPkField(@Nonnull Class<T> aClass) throws NoSuchFieldException;
}
