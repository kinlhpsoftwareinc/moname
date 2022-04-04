package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import com.kinlhp.moname.commons.jpa.Readable;

interface ReadableArchTests<T extends Readable<PK>, PK extends Serializable> {

	default void assertReadableArch(final Class<T> clazz) throws NoSuchMethodException {
		assertGetPkMethod(clazz);
		assertInherits(clazz);
		assertIsNotAnnotation(clazz);
		assertType(clazz);
		assertVisibility(clazz);
	}

	void assertGetPkMethod(Class<T> clazz) throws NoSuchMethodException;

	void assertInherits(Class<T> clazz);

	void assertIsNotAnnotation(Class<T> clazz);

	void assertType(Class<T> clazz);

	void assertVisibility(Class<T> clazz);

}
