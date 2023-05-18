package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.jpa.Readable;

interface ReadableArchTests<T extends Readable<PK>, PK extends Serializable> {

	default void assertReadableArch(@Nonnull final Class<T> aClass) throws NoSuchMethodException {
		assertGetPKMethod(aClass);
		assertInherits(aClass);
		assertIsNotAnnotation(aClass);
		assertType(aClass);
		assertVisibility(aClass);
	}

	void assertGetPKMethod(@Nonnull Class<T> aClass) throws NoSuchMethodException;

	void assertInherits(@Nonnull Class<T> aClass);

	void assertIsNotAnnotation(@Nonnull Class<T> aClass);

	void assertType(@Nonnull Class<T> aClass);

	void assertVisibility(@Nonnull Class<T> aClass);
}
