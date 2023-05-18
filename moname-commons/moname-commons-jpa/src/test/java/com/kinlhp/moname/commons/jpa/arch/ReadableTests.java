package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import jakarta.annotation.Nonnull;

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
	@ValueSource(classes = {Readable.class})
	public final void assertReadableArch(@Nonnull final Class<Readable<Serializable>> aClass) throws NoSuchMethodException {
		ReadableArchTests.super.assertReadableArch(aClass);
	}

	@DisplayName(value = "It has an {Serializable getPK()} declared method.")
	@Override
	public final void assertGetPKMethod(@Nonnull final Class<Readable<Serializable>> aClass) throws NoSuchMethodException {
		@Nonnull final var method = aClass.getDeclaredMethod("getPK");
		Assertions.assertAll("getPK()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPK()} Public visibility."),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPK()} With no one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPK()} Returns a Serializable.")
		);
	}

	@DisplayName(value = "It is inheriting only Serializable.")
	@Override
	public final void assertInherits(@Nonnull final Class<Readable<Serializable>> aClass) {
		@Nonnull final var superclasses = aClass.getInterfaces();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Serializable.class, superclasses[0], "It is inheriting Serializable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is inheriting only Serializable.")
		);
	}

	@DisplayName(value = "It is not an annotation.")
	@Override
	public final void assertIsNotAnnotation(@Nonnull final Class<Readable<Serializable>> aClass) {
		Assertions.assertFalse(aClass.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It's an interface.")
	@Override
	public final void assertType(@Nonnull final Class<Readable<Serializable>> aClass) {
		Assertions.assertTrue(aClass.isInterface(), "It's an interface.");
	}

	@DisplayName(value = "It's public visibility.")
	@Override
	public final void assertVisibility(@Nonnull final Class<Readable<Serializable>> aClass) {
		Assertions.assertEquals(TYPE_VISIBILITY, aClass.getModifiers(), "It's public visibility.");
	}
}
