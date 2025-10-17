package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;

import com.kinlhp.moname.commons.jpa.entity.AbstractPersistable;

@SuppressWarnings("java:S119")
interface AbstractPersistableArchTests<T extends AbstractPersistable<PK>, PK extends Serializable> extends
	PersistableArchTests<T, PK> {

	default void assertAbstractPersistableArch(final Class<T> type) throws NoSuchMethodException {
		PersistableArchTests.super.assertPersistableArch(type);
	}

	void assertImplements(Class<T> type);

	void assertIsAnnotated(Class<T> type);

	void assertPKField(Class<T> type) throws NoSuchFieldException;
}
