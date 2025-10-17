package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.jpa.entity.Persistable;

@SuppressWarnings("java:S119")
interface PersistableArchTests<T extends Persistable<PK>, PK extends Serializable> {

	default void assertPersistableArch(@Nonnull final Class<T> type) throws NoSuchMethodException {
		assertGetPkMethod(type);
		assertInherits(type);
		assertIsNotAnnotation(type);
		assertSetPkMethod(type);
		assertType(type);
		assertVisibility(type);
	}

	void assertGetPkMethod(@Nonnull Class<T> type) throws NoSuchMethodException;

	void assertInherits(@Nonnull Class<T> type);

	void assertIsNotAnnotation(@Nonnull Class<T> type);

	void assertSetPkMethod(@Nonnull Class<T> type) throws NoSuchMethodException;

	void assertType(@Nonnull Class<T> type);

	void assertVisibility(@Nonnull Class<T> type);
}
