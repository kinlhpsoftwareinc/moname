package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;


import com.kinlhp.moname.commons.jpa.entity.AbstractReadOnly;

@SuppressWarnings("java:S119")
interface AbstractReadOnlyArchTests<T extends AbstractReadOnly<PK>, PK extends Serializable> extends ReadableArchTests<T, PK> {

	default void assertAbstractReadOnlyArch(final Class<T> type) throws NoSuchMethodException, NoSuchFieldException {
		ReadableArchTests.super.assertReadableArch(type);
		assertImplements(type);
		assertIsAnnotated(type);
		assertPKField(type);
		assertReadOnlyListener(type);
		assertSetPkMethod(type);
	}

	void assertImplements(Class<T> type);

	void assertIsAnnotated(Class<T> type);

	void assertPKField(Class<T> type) throws NoSuchFieldException;

	void assertReadOnlyListener(Class<T> type);

	void assertSetPkMethod(Class<T> type) throws NoSuchMethodException;
}
