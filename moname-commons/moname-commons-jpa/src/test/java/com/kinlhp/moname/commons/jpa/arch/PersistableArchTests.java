package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.jpa.Persistable;

interface PersistableArchTests<T extends Persistable<PK>, PK extends Serializable> extends ReadableArchTests<T, PK> {

	default void assertPersistableArch(@Nonnull final Class<T> aClass) throws NoSuchMethodException {
		ReadableArchTests.super.assertReadableArch(aClass);
		assertSetPKMethod(aClass);
	}

	void assertSetPKMethod(@Nonnull Class<T> aClass) throws NoSuchMethodException;
}
