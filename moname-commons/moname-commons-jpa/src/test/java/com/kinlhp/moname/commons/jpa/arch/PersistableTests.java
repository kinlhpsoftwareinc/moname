package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import jakarta.validation.constraints.NotNull;

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
	@ValueSource(classes = { Persistable.class })
	public final void assertPersistableArch(final Class<Persistable<Serializable>> clazz) throws
		NoSuchMethodException {
		PersistableArchTests.super.assertPersistableArch(clazz);
	}

	@DisplayName(value = "It has an {Serializable getPk()} declared method.")
	@Override
	public final void assertGetPkMethod(final Class<Persistable<Serializable>> clazz) throws NoSuchMethodException {
		final var method = clazz.getMethod("getPk");
		Assertions.assertAll("getPk()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPk()} Public visibility."),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPk()} With no one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPk()} Returns a Serializable.")
		);
	}

	@DisplayName(value = "It is inheriting only Readable.")
	@Override
	public final void assertInherits(final Class<Persistable<Serializable>> clazz) {
		final var superclasses = clazz.getInterfaces();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Readable.class, superclasses[0], "It is inheriting Readable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is inheriting only Readable.")
		);
	}

	@DisplayName(value = "It is not an annotation.")
	@Override
	public final void assertIsNotAnnotation(final Class<Persistable<Serializable>> clazz) {
		Assertions.assertFalse(clazz.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It has an {void setPk(Serializable)} declared method.")
	@Override
	public final void assertSetPkMethod(final Class<Persistable<Serializable>> clazz) throws NoSuchMethodException {
		final var method = clazz.getDeclaredMethod("setPk", Serializable.class);
		Assertions.assertAll("setPk(Serializable)",
			() -> Assertions.assertEquals(SET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{setPk(Serializable)} Public visibility."),
			() -> Assertions.assertEquals(1, method.getParameterCount(),
				"{setPk(Serializable)} With only one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getParameters()[0].getType(),
				"{setPk(Serializable)} Serializable as first parameter."),
			() -> Assertions.assertEquals(Modifier.FINAL, (method.getParameters()[0].getModifiers() | Modifier.FINAL),
				"{setPk(Serializable)} First parameter is final."),
			() -> Assertions.assertNotNull(method.getParameters()[0].getDeclaredAnnotation(NotNull.class),
				"{setPk(Serializable)} First parameter annotated with NotNull."),
			() -> Assertions.assertEquals(1, method.getParameters()[0].getDeclaredAnnotations().length,
				"{setPk(Serializable)} First parameter annotated only with NotNull."),
			() -> Assertions.assertEquals(Void.TYPE, method.getReturnType(), "{setPk(Serializable)} Void return.")
		);
	}

	@DisplayName(value = "It's an interface.")
	@Override
	public final void assertType(final Class<Persistable<Serializable>> clazz) {
		Assertions.assertTrue(clazz.isInterface(), "It's an interface.");
	}

	@DisplayName(value = "It's public visibility.")
	@Override
	public final void assertVisibility(final Class<Persistable<Serializable>> clazz) {
		Assertions.assertEquals(TYPE_VISIBILITY, clazz.getModifiers(), "It's public visibility.");
	}

}
