package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.kinlhp.moname.commons.jpa.Readable;

/**
 * Architectural tests for abstraction of readable entities.
 */
class ReadableTests implements ReadableArchTests<Readable<Serializable>, Serializable> {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT | Modifier.INTERFACE;

	@Override
	@ParameterizedTest
	@ValueSource(classes = { Readable.class })
	public final void assertReadableArch(final Class<Readable<Serializable>> clazz) throws NoSuchMethodException {
		ReadableArchTests.super.assertReadableArch(clazz);
	}

	@DisplayName(value = "It has an {Serializable getPk()} declared method.")
	@Override
	public final void assertGetPkMethod(final Class<Readable<Serializable>> clazz) throws NoSuchMethodException {
		final var method = clazz.getDeclaredMethod("getPk");
		Assertions.assertAll("getPk()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPk()} Public visibility."),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPk()} With no one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPk()} Returns a Serializable.")
		);
	}

	@DisplayName(value = "It is inheriting only Serializable.")
	@Override
	public final void assertInherits(final Class<Readable<Serializable>> clazz) {
		final var superclasses = clazz.getInterfaces();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Serializable.class, superclasses[0], "It is inheriting Serializable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is inheriting only Serializable.")
		);
	}

	@DisplayName(value = "It is not an annotation.")
	@Override
	public final void assertIsNotAnnotation(final Class<Readable<Serializable>> clazz) {
		Assertions.assertFalse(clazz.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It's an interface.")
	@Override
	public final void assertType(final Class<Readable<Serializable>> clazz) {
		Assertions.assertTrue(clazz.isInterface(), "It's an interface.");
	}

	@DisplayName(value = "It's public visibility.")
	@Override
	public final void assertVisibility(final Class<Readable<Serializable>> clazz) {
		Assertions.assertEquals(TYPE_VISIBILITY, clazz.getModifiers(), "It's public visibility.");
	}

}
