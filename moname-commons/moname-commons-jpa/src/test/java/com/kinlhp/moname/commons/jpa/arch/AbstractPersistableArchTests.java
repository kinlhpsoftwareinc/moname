package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.jpa.entity.AbstractPersistable;

@SuppressWarnings("java:S119")
interface AbstractPersistableArchTests<T extends AbstractPersistable<PK>, PK extends Serializable> extends
	PersistableArchTests<T, PK> {

	default void assertAbstractPersistableArch(@Nonnull final Class<T> type) throws NoSuchMethodException {
		PersistableArchTests.super.assertPersistableArch(type);
	}

	void assertImplements(@Nonnull Class<T> type);

	void assertIsAnnotated(@Nonnull Class<T> type);

	void assertPKField(@Nonnull Class<T> type) throws NoSuchFieldException;
}
