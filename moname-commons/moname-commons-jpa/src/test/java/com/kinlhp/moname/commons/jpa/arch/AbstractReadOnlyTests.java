package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.kinlhp.moname.commons.jpa.AbstractReadOnly;
import com.kinlhp.moname.commons.jpa.ReadOnlyListener;
import com.kinlhp.moname.commons.jpa.Readable;

/**
 * Architectural tests for abstract implementation of read-only entities.
 */
class AbstractReadOnlyTests implements AbstractReadOnlyArchTests<AbstractReadOnly<Serializable>, Serializable> {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC;
	private static final int PK_FIELD_VISIBILITY = Modifier.PRIVATE;
	private static final int SET_PK_METHOD_VISIBILITY = Modifier.PUBLIC;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;

	@Override
	@ParameterizedTest
	@ValueSource(classes = { AbstractReadOnly.class })
	public final void assertAbstractReadOnlyArch(final Class<AbstractReadOnly<Serializable>> clazz) throws
		NoSuchMethodException, NoSuchFieldException {
		AbstractReadOnlyArchTests.super.assertAbstractReadOnlyArch(clazz);
	}

	@DisplayName(value = "It has an {Serializable getPk()} method.")
	@Override
	public final void assertGetPkMethod(final Class<AbstractReadOnly<Serializable>> clazz) throws
		NoSuchMethodException {
		final var method = clazz.getDeclaredMethod("getPk");
		Assertions.assertAll("getPk()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPk()} Public visibility."),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPk()} With no one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPk()} Returns a Serializable.")
		);
	}

	@DisplayName(value = "It is implementing only Readable.")
	@Override
	public final void assertImplements(final Class<AbstractReadOnly<Serializable>> clazz) {
		final var superclasses = clazz.getInterfaces();
		Assertions.assertAll("implementing",
			() -> Assertions.assertEquals(Readable.class, superclasses[0], "It is implementing Readable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is implementing only Readable.")
		);
	}

	@DisplayName(value = "It is inheriting Object.")
	@Override
	public final void assertInherits(final Class<AbstractReadOnly<Serializable>> clazz) {
		final var superclass = clazz.getSuperclass();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Object.class, superclass, "It is inheriting Object.")
		);
	}

	@DisplayName(value = "It is annotated.")
	@Override
	public final void assertIsAnnotated(final Class<AbstractReadOnly<Serializable>> clazz) {
		MatcherAssert.assertThat("It is annotated.",
			Arrays.stream(clazz.getDeclaredAnnotations()).map(Annotation::annotationType)
				.collect(Collectors.toUnmodifiableSet()),
			Matchers.containsInAnyOrder(EntityListeners.class, MappedSuperclass.class)
		);
	}

	@DisplayName(value = "It is not an annotation.")
	@Override
	public final void assertIsNotAnnotation(final Class<AbstractReadOnly<Serializable>> clazz) {
		Assertions.assertFalse(clazz.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It has an {Serializable pk} declared field.")
	@Override
	public final void assertPkField(final Class<AbstractReadOnly<Serializable>> clazz) throws NoSuchFieldException {
		final var field = clazz.getDeclaredField("pk");
		Assertions.assertAll("pk",
			() -> Assertions.assertEquals(PK_FIELD_VISIBILITY, field.getModifiers(), "{pk} Private visibility."),
			() -> Assertions.assertEquals(Serializable.class, field.getType(), "{pk} Serializable."),
			() -> Assertions.assertNotNull(field.getDeclaredAnnotationsByType(Id.class), "{pk} Id annotation."),
			() -> Assertions.assertEquals(1, field.getDeclaredAnnotations().length, "{pk} Id annotation only.")
		);
	}

	@DisplayName(value = "It is listened only from ReadOnlyListener.")
	@Override
	public final void assertReadOnlyListener(final Class<AbstractReadOnly<Serializable>> clazz) {
		final var callbacks = clazz.getDeclaredAnnotation(EntityListeners.class).value();
		Assertions.assertAll("callbacks",
			() -> Assertions.assertEquals(ReadOnlyListener.class, callbacks[0],
				"It is listened from ReadOnlyListener."),
			() -> Assertions.assertEquals(1, callbacks.length, "It is listened only from ReadOnlyListener.")
		);
	}

	@DisplayName(value = "It has an {void setPk(Serializable)} declared method.")
	@Override
	public final void assertSetPkMethod(final Class<AbstractReadOnly<Serializable>> clazz) throws
		NoSuchMethodException {
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

	@DisplayName(value = "It's an class.")
	@Override
	public final void assertType(final Class<AbstractReadOnly<Serializable>> clazz) {
		Assertions.assertFalse(clazz.isInterface(), "It's an class.");
	}

	@DisplayName(value = "It's abstract with public visibility.")
	@Override
	public final void assertVisibility(final Class<AbstractReadOnly<Serializable>> clazz) {
		Assertions.assertEquals(TYPE_VISIBILITY, clazz.getModifiers(), "It's abstract with public visibility.");
	}

}
