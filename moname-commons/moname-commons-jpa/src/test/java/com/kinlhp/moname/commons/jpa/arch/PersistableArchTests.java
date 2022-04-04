package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import com.kinlhp.moname.commons.jpa.Persistable;

interface PersistableArchTests<T extends Persistable<PK>, PK extends Serializable> extends ReadableArchTests<T, PK> {

	default void assertPersistableArch(final Class<T> clazz) throws NoSuchMethodException {
		ReadableArchTests.super.assertReadableArch(clazz);
		assertSetPkMethod(clazz);
	}

	void assertSetPkMethod(Class<T> clazz) throws NoSuchMethodException;

}
