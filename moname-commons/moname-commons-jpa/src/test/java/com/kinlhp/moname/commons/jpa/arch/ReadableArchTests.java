package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.jpa.entity.Readable;

@SuppressWarnings("java:S119")
interface ReadableArchTests<T extends Readable<PK>, PK extends Serializable> {

	default void assertReadableArch(@Nonnull final Class<T> type) throws NoSuchMethodException {
		assertGetPkMethod(type);
		assertInherits(type);
		assertIsNotAnnotation(type);
		assertType(type);
		assertVisibility(type);
	}

	void assertGetPkMethod(@Nonnull Class<T> type) throws NoSuchMethodException;

	void assertInherits(@Nonnull Class<T> type);

	void assertIsNotAnnotation(@Nonnull Class<T> type);

	void assertType(@Nonnull Class<T> type);

	void assertVisibility(@Nonnull Class<T> type);
}
