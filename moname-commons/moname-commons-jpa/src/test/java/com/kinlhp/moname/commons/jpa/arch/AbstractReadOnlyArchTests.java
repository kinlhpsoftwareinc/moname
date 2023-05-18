package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.jpa.AbstractReadOnly;

interface AbstractReadOnlyArchTests<T extends AbstractReadOnly<PK>, PK extends Serializable> extends ReadableArchTests<T, PK> {

	default void assertAbstractReadOnlyArch(@Nonnull final Class<T> aClass) throws NoSuchMethodException, NoSuchFieldException {
		ReadableArchTests.super.assertReadableArch(aClass);
		assertImplements(aClass);
		assertIsAnnotated(aClass);
		assertPkField(aClass);
		assertReadOnlyListener(aClass);
		assertSetPKMethod(aClass);
	}

	void assertImplements(@Nonnull Class<T> aClass);

	void assertIsAnnotated(@Nonnull Class<T> aClass);

	void assertPkField(@Nonnull Class<T> aClass) throws NoSuchFieldException;

	void assertReadOnlyListener(@Nonnull Class<T> aClass);

	void assertSetPKMethod(@Nonnull Class<T> aClass) throws NoSuchMethodException;
}
