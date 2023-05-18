package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;
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
	@ValueSource(classes = {AbstractReadOnly.class})
	public final void assertAbstractReadOnlyArch(@Nonnull final Class<AbstractReadOnly<Serializable>> aClass) throws
		NoSuchMethodException, NoSuchFieldException {
		AbstractReadOnlyArchTests.super.assertAbstractReadOnlyArch(aClass);
	}

	@DisplayName(value = "It has an {Serializable getPK()} method.")
	@Override
	public final void assertGetPKMethod(@Nonnull final Class<AbstractReadOnly<Serializable>> aClass) throws
		NoSuchMethodException {
		@Nonnull final var method = aClass.getDeclaredMethod("getPK");
		Assertions.assertAll("getPK()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPK()} Public visibility."),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPK()} With no one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPK()} Returns a Serializable.")
		);
	}

	@DisplayName(value = "It is implementing only Readable.")
	@Override
	public final void assertImplements(@Nonnull final Class<AbstractReadOnly<Serializable>> aClass) {
		@Nonnull final var superclasses = aClass.getInterfaces();
		Assertions.assertAll("implementing",
			() -> Assertions.assertEquals(Readable.class, superclasses[0], "It is implementing Readable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is implementing only Readable.")
		);
	}

	@DisplayName(value = "It is inheriting Object.")
	@Override
	public final void assertInherits(@Nonnull final Class<AbstractReadOnly<Serializable>> aClass) {
		@Nonnull final var superclass = aClass.getSuperclass();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Object.class, superclass, "It is inheriting Object.")
		);
	}

	@DisplayName(value = "It is annotated.")
	@Override
	public final void assertIsAnnotated(@Nonnull final Class<AbstractReadOnly<Serializable>> aClass) {
		MatcherAssert.assertThat("It is annotated.",
			Arrays.stream(aClass.getDeclaredAnnotations()).map(Annotation::annotationType)
				.collect(Collectors.toUnmodifiableSet()),
			Matchers.containsInAnyOrder(EntityListeners.class, MappedSuperclass.class)
		);
	}

	@DisplayName(value = "It is not an annotation.")
	@Override
	public final void assertIsNotAnnotation(@Nonnull final Class<AbstractReadOnly<Serializable>> aClass) {
		Assertions.assertFalse(aClass.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It has an {Serializable pk} declared field.")
	@Override
	public final void assertPkField(@Nonnull final Class<AbstractReadOnly<Serializable>> aClass) throws NoSuchFieldException {
		@Nonnull final var field = aClass.getDeclaredField("pk");
		Assertions.assertAll("pk",
			() -> Assertions.assertEquals(PK_FIELD_VISIBILITY, field.getModifiers(), "{pk} Private visibility."),
			() -> Assertions.assertEquals(Serializable.class, field.getType(), "{pk} Serializable."),
			() -> Assertions.assertNotNull(field.getDeclaredAnnotationsByType(Id.class), "{pk} ID annotation."),
			() -> Assertions.assertEquals(2, field.getDeclaredAnnotations().length, "{pk} ID annotation only."),
			() -> Assertions.assertNotNull(field.getDeclaredAnnotation(Id.class)),
			() -> Assertions.assertNotNull(field.getDeclaredAnnotation(NotNull.class))
		);
	}

	@DisplayName(value = "It is listened only from ReadOnlyListener.")
	@Override
	public final void assertReadOnlyListener(@Nonnull final Class<AbstractReadOnly<Serializable>> aClass) {
		@Nonnull final var callbacks = aClass.getDeclaredAnnotation(EntityListeners.class).value();
		Assertions.assertAll("callbacks",
			() -> Assertions.assertEquals(ReadOnlyListener.class, callbacks[0],
				"It is listened from ReadOnlyListener."),
			() -> Assertions.assertEquals(1, callbacks.length, "It is listened only from ReadOnlyListener.")
		);
	}

	@DisplayName(value = "It has an {void setPK(Serializable)} declared method.")
	@Override
	public final void assertSetPKMethod(@Nonnull final Class<AbstractReadOnly<Serializable>> aClass) throws
		NoSuchMethodException {
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

	@DisplayName(value = "It's a class.")
	@Override
	public final void assertType(@Nonnull final Class<AbstractReadOnly<Serializable>> aClass) {
		Assertions.assertFalse(aClass.isInterface(), "It's a class.");
	}

	@DisplayName(value = "It's abstract with public visibility.")
	@Override
	public final void assertVisibility(@Nonnull final Class<AbstractReadOnly<Serializable>> aClass) {
		Assertions.assertEquals(TYPE_VISIBILITY, aClass.getModifiers(), "It's abstract with public visibility.");
	}
}
