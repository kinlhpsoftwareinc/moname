package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import com.kinlhp.moname.commons.jpa.entity.Persistable;

@SuppressWarnings("java:S119")
interface PersistableArchTests<T extends Persistable<PK>, PK extends Serializable> {

	default void assertPersistableArch(final Class<T> type) throws NoSuchMethodException {
		assertGetPkMethod(type);
		assertInherits(type);
		assertIsNotAnnotation(type);
		assertSetPkMethod(type);
		assertType(type);
		assertVisibility(type);
	}

	void assertGetPkMethod(Class<T> type) throws NoSuchMethodException;

	void assertInherits(Class<T> type);

	void assertIsNotAnnotation(Class<T> type);

	void assertSetPkMethod(Class<T> type) throws NoSuchMethodException;

	void assertType(Class<T> type);

	void assertVisibility(Class<T> type);
}
