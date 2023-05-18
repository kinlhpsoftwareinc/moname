package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import jakarta.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.kinlhp.moname.commons.jpa.Persistable;
import com.kinlhp.moname.commons.jpa.Readable;

/**
 * Architectural tests for abstraction of persistable entities.
 */
class PersistableTests implements PersistableArchTests<Persistable<Serializable>, Serializable> {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int SET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT | Modifier.INTERFACE;

	@Override
	@ParameterizedTest
	@ValueSource(classes = {Persistable.class})
	public final void assertPersistableArch(@Nonnull final Class<Persistable<Serializable>> aClass) throws
		NoSuchMethodException {
		PersistableArchTests.super.assertPersistableArch(aClass);
	}

	@DisplayName(value = "It has an {Serializable getPK()} declared method.")
	@Override
	public final void assertGetPKMethod(@Nonnull final Class<Persistable<Serializable>> aClass) throws NoSuchMethodException {
		@Nonnull final var method = aClass.getMethod("getPK");
		Assertions.assertAll("getPK()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPK()} Public visibility."),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPK()} With no one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPK()} Returns a Serializable.")
		);
	}

	@DisplayName(value = "It is inheriting only Readable.")
	@Override
	public final void assertInherits(@Nonnull final Class<Persistable<Serializable>> aClass) {
		@Nonnull final var superclasses = aClass.getInterfaces();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Readable.class, superclasses[0], "It is inheriting Readable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is inheriting only Readable.")
		);
	}

	@DisplayName(value = "It is not an annotation.")
	@Override
	public final void assertIsNotAnnotation(@Nonnull final Class<Persistable<Serializable>> aClass) {
		Assertions.assertFalse(aClass.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It has an {void setPK(Serializable)} declared method.")
	@Override
	public final void assertSetPKMethod(@Nonnull final Class<Persistable<Serializable>> aClass) throws NoSuchMethodException {
		@Nonnull final var method = aClass.getDeclaredMethod("setPK", Serializable.class);
		Assertions.assertAll("setPK(Serializable)",
			() -> Assertions.assertEquals(SET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{setPK(Serializable)} Public visibility."),
			() -> Assertions.assertEquals(1, method.getParameterCount(),
				"{setPK(Serializable)} With only one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getParameters()[0].getType(),
				"{setPK(Serializable)} Serializable as first parameter."),
			() -> Assertions.assertEquals(Modifier.FINAL, (method.getParameters()[0].getModifiers() | Modifier.FINAL),
				"{setPK(Serializable)} First parameter is final."),
			() -> Assertions.assertNotNull(method.getParameters()[0].getDeclaredAnnotation(Nonnull.class),
				"{setPK(Serializable)} First parameter annotated with NotNull."),
			() -> Assertions.assertEquals(1, method.getParameters()[0].getDeclaredAnnotations().length,
				"{setPK(Serializable)} First parameter annotated only with NotNull."),
			() -> Assertions.assertEquals(Void.TYPE, method.getReturnType(), "{setPK(Serializable)} Void return.")
		);
	}

	@DisplayName(value = "It's an interface.")
	@Override
	public final void assertType(@Nonnull final Class<Persistable<Serializable>> aClass) {
		Assertions.assertTrue(aClass.isInterface(), "It's an interface.");
	}

	@DisplayName(value = "It's public visibility.")
	@Override
	public final void assertVisibility(@Nonnull final Class<Persistable<Serializable>> aClass) {
		Assertions.assertEquals(TYPE_VISIBILITY, aClass.getModifiers(), "It's public visibility.");
	}
}
