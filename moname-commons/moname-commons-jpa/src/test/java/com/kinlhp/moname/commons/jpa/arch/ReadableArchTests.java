package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import com.kinlhp.moname.commons.jpa.entity.Readable;

@SuppressWarnings("java:S119")
interface ReadableArchTests<T extends Readable<PK>, PK extends Serializable> {

	default void assertReadableArch(final Class<T> type) throws NoSuchMethodException {
		assertGetPkMethod(type);
		assertInherits(type);
		assertIsNotAnnotation(type);
		assertType(type);
		assertVisibility(type);
	}

	void assertGetPkMethod(Class<T> type) throws NoSuchMethodException;

	void assertInherits(Class<T> type);

	void assertIsNotAnnotation(Class<T> type);

	void assertType(Class<T> type);

	void assertVisibility(Class<T> type);
}
