package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import com.kinlhp.moname.commons.jpa.AbstractReadOnly;

interface AbstractReadOnlyArchTests<T extends AbstractReadOnly<PK>, PK extends Serializable> extends
	ReadableArchTests<T, PK> {

	default void assertAbstractReadOnlyArch(final Class<T> clazz) throws NoSuchMethodException, NoSuchFieldException {
		ReadableArchTests.super.assertReadableArch(clazz);
		assertImplements(clazz);
		assertIsAnnotated(clazz);
		assertPkField(clazz);
		assertReadOnlyListener(clazz);
		assertSetPkMethod(clazz);
	}

	void assertImplements(Class<T> clazz);

	void assertIsAnnotated(Class<T> clazz);

	void assertPkField(Class<T> clazz) throws NoSuchFieldException;

	void assertReadOnlyListener(Class<T> clazz);

	void assertSetPkMethod(Class<T> clazz) throws NoSuchMethodException;

}
