package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Access;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.kinlhp.moname.commons.jpa.entity.AbstractReadOnly;
import com.kinlhp.moname.commons.jpa.entity.Readable;
import com.kinlhp.moname.commons.jpa.listener.ReadOnlyListener;

/**
 * Architectural tests for abstract implementation of read-only entities.
 */
class AbstractReadOnlyTests implements AbstractReadOnlyArchTests<AbstractReadOnly<Serializable>, Serializable> {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int PK_FIELD_VISIBILITY = Modifier.PRIVATE;
	private static final int SET_PK_METHOD_VISIBILITY = Modifier.PUBLIC;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;

	@Override
	@ParameterizedTest
	@ValueSource(classes = AbstractReadOnly.class)
	public final void assertAbstractReadOnlyArch(@Nonnull final Class<AbstractReadOnly<Serializable>> type) throws
		NoSuchMethodException, NoSuchFieldException {
		AbstractReadOnlyArchTests.super.assertAbstractReadOnlyArch(type);
	}

	@DisplayName("It has an {Serializable getPk()} method")
	@Override
	public final void assertGetPkMethod(@Nonnull final Class<AbstractReadOnly<Serializable>> type) throws
		NoSuchMethodException {
		@Nonnull final var method = type.getMethod("getPk");
		Assertions.assertAll("getPk()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPk()} Public visibility"),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPk()} With no one parameter"),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPk()} Returns a Serializable")
		);
	}

	@DisplayName("It is implementing only Readable")
	@Override
	public final void assertImplements(@Nonnull final Class<AbstractReadOnly<Serializable>> type) {
		@Nonnull final var superclasses = type.getInterfaces();
		Assertions.assertAll("implementing",
			() -> Assertions.assertEquals(Readable.class, superclasses[0], "It is implementing Readable"),
			() -> Assertions.assertEquals(1, superclasses.length, "It is implementing only Readable")
		);
	}

	@DisplayName("It is inheriting Object")
	@Override
	public final void assertInherits(@Nonnull final Class<AbstractReadOnly<Serializable>> type) {
		@Nonnull final var superclass = type.getSuperclass();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Object.class, superclass, "It is inheriting Object")
		);
	}

	@DisplayName("It is annotated")
	@Override
	public final void assertIsAnnotated(@Nonnull final Class<AbstractReadOnly<Serializable>> type) {
		MatcherAssert.assertThat("It is annotated",
			Arrays.stream(type.getDeclaredAnnotations()).map(Annotation::annotationType)
				.collect(Collectors.toUnmodifiableSet()),
			Matchers.containsInAnyOrder(EntityListeners.class, MappedSuperclass.class)
		);
	}

	@DisplayName("It is not an annotation")
	@Override
	public final void assertIsNotAnnotation(@Nonnull final Class<AbstractReadOnly<Serializable>> type) {
		Assertions.assertFalse(type.isAnnotation(), "It is not an annotation");
	}

	@DisplayName("It has an {Serializable pk} declared field")
	@Override
	public final void assertPKField(@Nonnull final Class<AbstractReadOnly<Serializable>> type) throws NoSuchFieldException {
		@Nonnull final var field = type.getDeclaredField("pk");
		Assertions.assertAll("pk",
			() -> Assertions.assertEquals(PK_FIELD_VISIBILITY, field.getModifiers(), "{pk} Private visibility"),
			() -> Assertions.assertEquals(Serializable.class, field.getType(), "{pk} Serializable"),
			() -> Assertions.assertNotNull(field.getDeclaredAnnotationsByType(Id.class), "{pk} ID annotation"),
			() -> Assertions.assertEquals(1, field.getDeclaredAnnotations().length, "{pk} ID annotation only"),
			() -> Assertions.assertNotNull(field.getDeclaredAnnotation(Access.class))
		);
	}

	@DisplayName("It is listened only from ReadOnlyListener")
	@Override
	public final void assertReadOnlyListener(@Nonnull final Class<AbstractReadOnly<Serializable>> type) {
		@Nonnull final var callbacks = type.getDeclaredAnnotation(EntityListeners.class).value();
		Assertions.assertAll("callbacks",
			() -> Assertions.assertEquals(ReadOnlyListener.class, callbacks[0],
				"It is listened from ReadOnlyListener"),
			() -> Assertions.assertEquals(1, callbacks.length, "It is listened only from ReadOnlyListener")
		);
	}

	@DisplayName("It has an {void setPk(Serializable)} declared method")
	@Override
	public final void assertSetPkMethod(@Nonnull final Class<AbstractReadOnly<Serializable>> type) throws
		NoSuchMethodException {
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

	@DisplayName("It's a class")
	@Override
	public final void assertType(@Nonnull final Class<AbstractReadOnly<Serializable>> type) {
		Assertions.assertFalse(type.isInterface(), "It's a class");
	}

	@DisplayName("It's abstract with public visibility")
	@Override
	public final void assertVisibility(@Nonnull final Class<AbstractReadOnly<Serializable>> type) {
		Assertions.assertEquals(TYPE_VISIBILITY, type.getModifiers(), "It's abstract with public visibility");
	}
}
