package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.jpa.entity.AbstractReadOnly;

@SuppressWarnings("java:S119")
interface AbstractReadOnlyArchTests<T extends AbstractReadOnly<PK>, PK extends Serializable> extends ReadableArchTests<T, PK> {

	default void assertAbstractReadOnlyArch(@Nonnull final Class<T> type) throws NoSuchMethodException, NoSuchFieldException {
		ReadableArchTests.super.assertReadableArch(type);
		assertImplements(type);
		assertIsAnnotated(type);
		assertPKField(type);
		assertReadOnlyListener(type);
		assertSetPkMethod(type);
	}

	void assertImplements(@Nonnull Class<T> type);

	void assertIsAnnotated(@Nonnull Class<T> type);

	void assertPKField(@Nonnull Class<T> type) throws NoSuchFieldException;

	void assertReadOnlyListener(@Nonnull Class<T> type);

	void assertSetPkMethod(@Nonnull Class<T> type) throws NoSuchMethodException;
}
