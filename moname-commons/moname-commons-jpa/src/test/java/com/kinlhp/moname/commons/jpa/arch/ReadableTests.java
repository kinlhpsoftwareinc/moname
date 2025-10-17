package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import jakarta.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.kinlhp.moname.commons.jpa.entity.Readable;

/**
 * Architectural tests for abstraction of readable entities.
 */
class ReadableTests implements ReadableArchTests<Readable<Serializable>, Serializable> {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT | Modifier.INTERFACE;

	@Override
	@ParameterizedTest
	@ValueSource(classes = Readable.class)
	public final void assertReadableArch(@Nonnull final Class<Readable<Serializable>> type) throws NoSuchMethodException {
		ReadableArchTests.super.assertReadableArch(type);
	}

	@DisplayName("It has an {Serializable getPk()} declared method")
	@Override
	public final void assertGetPkMethod(@Nonnull final Class<Readable<Serializable>> type) throws NoSuchMethodException {
		@Nonnull final var method = type.getDeclaredMethod("getPk");
		Assertions.assertAll("getPk()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPk()} Public visibility"),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPk()} With no one parameter"),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPk()} Returns a Serializable")
		);
	}

	@DisplayName("It is inheriting only Serializable")
	@Override
	public final void assertInherits(@Nonnull final Class<Readable<Serializable>> type) {
		@Nonnull final var superclasses = type.getInterfaces();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Serializable.class, superclasses[0], "It is inheriting Serializable"),
			() -> Assertions.assertEquals(1, superclasses.length, "It is inheriting only Serializable")
		);
	}

	@DisplayName("It is not an annotation")
	@Override
	public final void assertIsNotAnnotation(@Nonnull final Class<Readable<Serializable>> type) {
		Assertions.assertFalse(type.isAnnotation(), "It is not an annotation");
	}

	@DisplayName("It's an interface")
	@Override
	public final void assertType(@Nonnull final Class<Readable<Serializable>> type) {
		Assertions.assertTrue(type.isInterface(), "It's an interface");
	}

	@DisplayName("It's public visibility")
	@Override
	public final void assertVisibility(@Nonnull final Class<Readable<Serializable>> type) {
		Assertions.assertEquals(TYPE_VISIBILITY, type.getModifiers(), "It's public visibility");
	}
}
