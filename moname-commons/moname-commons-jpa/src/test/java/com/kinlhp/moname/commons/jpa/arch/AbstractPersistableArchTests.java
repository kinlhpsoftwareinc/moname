package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import com.kinlhp.moname.commons.jpa.AbstractPersistable;

interface AbstractPersistableArchTests<T extends AbstractPersistable<PK>, PK extends Serializable> extends
	PersistableArchTests<T, PK> {

	default void assertAbstractPersistableArch(final Class<T> clazz) throws NoSuchMethodException {
		PersistableArchTests.super.assertPersistableArch(clazz);
	}

	void assertImplements(Class<T> clazz);

	void assertIsAnnotated(Class<T> clazz);

	void assertPkField(Class<T> clazz) throws NoSuchFieldException;

}
