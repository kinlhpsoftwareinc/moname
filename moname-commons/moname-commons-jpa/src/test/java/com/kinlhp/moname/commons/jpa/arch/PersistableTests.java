package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import jakarta.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.kinlhp.moname.commons.jpa.entity.Persistable;

/**
 * Architectural tests for abstraction of persistable entities.
 */
class PersistableTests implements PersistableArchTests<Persistable<Serializable>, Serializable> {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int SET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT | Modifier.INTERFACE;

	@Override
	@ParameterizedTest
	@ValueSource(classes = Persistable.class)
	public final void assertPersistableArch(@Nonnull final Class<Persistable<Serializable>> type) throws
		NoSuchMethodException {
		PersistableArchTests.super.assertPersistableArch(type);
	}

	@DisplayName("It has an {Serializable getPk()} declared method")
	@Override
	public final void assertGetPkMethod(@Nonnull final Class<Persistable<Serializable>> type) throws NoSuchMethodException {
		@Nonnull final var method = type.getMethod("getPk");
		Assertions.assertAll("getPk()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPk()} Public visibility"),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPk()} With no one parameter"),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPk()} Returns a Serializable")
		);
	}

	@DisplayName("It is inheriting Serializable")
	@Override
	public final void assertInherits(@Nonnull final Class<Persistable<Serializable>> type) {
		@Nonnull final var superclasses = type.getInterfaces();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(1, superclasses.length, ""),
			() -> Assertions.assertEquals(Serializable.class, superclasses[0], "It is inheriting Serializable")
		);
	}

	@DisplayName("It is not an annotation")
	@Override
	public final void assertIsNotAnnotation(@Nonnull final Class<Persistable<Serializable>> type) {
		Assertions.assertFalse(type.isAnnotation(), "It is not an annotation");
	}

	@DisplayName("It has an {void setPk(Serializable)} declared method")
	@Override
	public final void assertSetPkMethod(@Nonnull final Class<Persistable<Serializable>> type) throws NoSuchMethodException {
		@Nonnull final var method = type.getDeclaredMethod("setPk", Serializable.class);
		Assertions.assertAll("setPk(Serializable)",
			() -> Assertions.assertEquals(SET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{setPk(Serializable)} Public visibility"),
			() -> Assertions.assertEquals(1, method.getParameterCount(),
				"{setPk(Serializable)} With only one parameter"),
			() -> Assertions.assertEquals(Serializable.class, method.getParameters()[0].getType(),
				"{setPk(Serializable)} Serializable as first parameter"),
			() -> Assertions.assertEquals(Modifier.FINAL, (method.getParameters()[0].getModifiers() | Modifier.FINAL),
				"{setPk(Serializable)} First parameter is final"),
			() -> Assertions.assertNotNull(method.getParameters()[0].getDeclaredAnnotation(Nonnull.class),
				"{setPk(Serializable)} First parameter annotated with NotNull"),
			() -> Assertions.assertEquals(1, method.getParameters()[0].getDeclaredAnnotations().length,
				"{setPk(Serializable)} First parameter annotated only with NotNull"),
			() -> Assertions.assertEquals(Void.TYPE, method.getReturnType(), "{setPk(Serializable)} Void return")
		);
	}

	@DisplayName("It's an interface")
	@Override
	public final void assertType(@Nonnull final Class<Persistable<Serializable>> type) {
		Assertions.assertTrue(type.isInterface(), "It's an interface");
	}

	@DisplayName("It's public visibility")
	@Override
	public final void assertVisibility(@Nonnull final Class<Persistable<Serializable>> type) {
		Assertions.assertEquals(TYPE_VISIBILITY, type.getModifiers(), "It's public visibility");
	}
}
