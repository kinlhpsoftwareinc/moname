package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.kinlhp.moname.commons.jpa.AbstractPersistable;
import com.kinlhp.moname.commons.jpa.Persistable;

/**
 * Architectural tests for abstract implementation of persistable entities.
 */
class AbstractPersistableTests implements AbstractPersistableArchTests<AbstractPersistable<Serializable>, Serializable> {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int PK_FIELD_VISIBILITY = Modifier.PRIVATE;
	private static final int SET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;

	@Override
	@ParameterizedTest
	@ValueSource(classes = {AbstractPersistable.class})
	public final void assertAbstractPersistableArch(@Nonnull final Class<AbstractPersistable<Serializable>> aClass) throws
		NoSuchMethodException {
		AbstractPersistableArchTests.super.assertAbstractPersistableArch(aClass);
	}

	@DisplayName(value = "It has an {Serializable getPK()} method.")
	@Override
	public final void assertGetPKMethod(@Nonnull final Class<AbstractPersistable<Serializable>> aClass) throws
		NoSuchMethodException {
		@Nonnull final var method = aClass.getMethod("getPK");
		Assertions.assertAll("getPK()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPK()} Public visibility."),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPK()} With no one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPK()} Returns a Serializable.")
		);
	}

	@DisplayName(value = "It is implementing only Persistable.")
	@Override
	public final void assertImplements(@Nonnull final Class<AbstractPersistable<Serializable>> aClass) {
		@Nonnull final var superclasses = aClass.getInterfaces();
		Assertions.assertAll("implementing",
			() -> Assertions.assertEquals(Persistable.class, superclasses[0], "It is implementing Persistable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is implementing only Persistable.")
		);
	}

	@DisplayName(value = "It is inheriting Object.")
	@Override
	public final void assertInherits(@Nonnull final Class<AbstractPersistable<Serializable>> aClass) {
		@Nonnull final var superclass = aClass.getSuperclass();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Object.class, superclass, "It is inheriting Object.")
		);
	}

	@DisplayName(value = "It is annotated.")
	@Override
	public final void assertIsAnnotated(@Nonnull final Class<AbstractPersistable<Serializable>> aClass) {
		@Nonnull final var annotations = Arrays.stream(aClass.getDeclaredAnnotations()).map(Annotation::annotationType)
			.collect(Collectors.toUnmodifiableSet());
		@Nonnull final var expected = Set.of(MappedSuperclass.class);
		Assertions.assertEquals(expected, annotations, "It is annotated.");
	}

	@DisplayName(value = "It is not an annotation.")
	@Override
	public final void assertIsNotAnnotation(@Nonnull final Class<AbstractPersistable<Serializable>> aClass) {
		Assertions.assertFalse(aClass.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It has an {Serializable pk} declared field.")
	@Override
	public final void assertPkField(@Nonnull final Class<AbstractPersistable<Serializable>> aClass) throws NoSuchFieldException {
		@Nonnull final var field = aClass.getDeclaredField("pk");
		Assertions.assertAll("pk",
			() -> Assertions.assertEquals(PK_FIELD_VISIBILITY, field.getModifiers(), "{pk} Private visibility."),
			() -> Assertions.assertEquals(Serializable.class, field.getType(), "{pk} Serializable."),
			() -> MatcherAssert.assertThat("{pk} is annotated.",
				Arrays.stream(field.getDeclaredAnnotations()).map(Annotation::annotationType)
					.collect(Collectors.toUnmodifiableSet()),
				Matchers.containsInAnyOrder(Column.class, GeneratedValue.class, Id.class)),
			() -> Assertions.assertFalse(field.getDeclaredAnnotation(Column.class).updatable(),
				"{pk} Is not updatable."),
			() -> Assertions.assertEquals(GenerationType.IDENTITY, field.getDeclaredAnnotation(GeneratedValue.class)
				.strategy(), "{pk} Generation strategy using a database identity column.")
		);
	}

	@DisplayName(value = "It has an {void setPK(Serializable)} declared method.")
	@Override
	public final void assertSetPKMethod(@Nonnull final Class<AbstractPersistable<Serializable>> aClass) throws
		NoSuchMethodException {
		@Nonnull final var method = aClass.getMethod("setPK", Serializable.class);
		Assertions.assertAll("setPK(Serializable)",
			() -> Assertions.assertEquals(SET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{setPK(Serializable)} Public visibility."),
			() -> Assertions.assertEquals(1, method.getParameterCount(),
				"{setPK(Serializable)} With only one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getParameters()[0].getType(),
				"{setPK(Serializable)} Serializable as first parameter."),
			() -> Assertions.assertNotNull(method.getParameters()[0].getDeclaredAnnotation(Nonnull.class),
				"{setPK(Serializable)} First parameter annotated with NotNull."),
			() -> Assertions.assertEquals(1, method.getParameters()[0].getDeclaredAnnotations().length,
				"{setPK(Serializable)} First parameter annotated only with NotNull."),
			() -> Assertions.assertEquals(Void.TYPE, method.getReturnType(), "{setPK(Serializable)} Void return.")
		);
	}

	@DisplayName(value = "It's a class.")
	@Override
	public final void assertType(@Nonnull final Class<AbstractPersistable<Serializable>> aClass) {
		Assertions.assertFalse(aClass.isInterface(), "It's a class.");
	}

	@DisplayName(value = "It's abstract with public visibility.")
	@Override
	public final void assertVisibility(@Nonnull final Class<AbstractPersistable<Serializable>> aClass) {
		Assertions.assertEquals(TYPE_VISIBILITY, aClass.getModifiers(), "It's abstract with public visibility.");
	}
}
