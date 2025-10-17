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

import com.kinlhp.moname.commons.jpa.entity.AbstractPersistable;
import com.kinlhp.moname.commons.jpa.entity.Persistable;

/**
 * Architectural tests for abstract implementation of persistable entities.
 */
class AbstractPersistableTests implements AbstractPersistableArchTests<AbstractPersistable<Serializable>, Serializable> {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int PK_FIELD_VISIBILITY = Modifier.PRIVATE;
	private static final int SET_PK_METHOD_VISIBILITY = Modifier.PUBLIC;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;

	@Override
	@ParameterizedTest
	@ValueSource(classes = AbstractPersistable.class)
	public final void assertAbstractPersistableArch(@Nonnull final Class<AbstractPersistable<Serializable>> type) throws
		NoSuchMethodException {
		AbstractPersistableArchTests.super.assertAbstractPersistableArch(type);
	}

	@DisplayName("It has an {Serializable getPk()} method")
	@Override
	public final void assertGetPkMethod(@Nonnull final Class<AbstractPersistable<Serializable>> type) throws
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

	@DisplayName("It is implementing only Persistable")
	@Override
	public final void assertImplements(@Nonnull final Class<AbstractPersistable<Serializable>> type) {
		@Nonnull final var superclasses = type.getInterfaces();
		Assertions.assertAll("implementing",
			() -> Assertions.assertEquals(Persistable.class, superclasses[0], "It is implementing Persistable"),
			() -> Assertions.assertEquals(1, superclasses.length, "It is implementing only Persistable")
		);
	}

	@DisplayName("It is inheriting Object")
	@Override
	public final void assertInherits(@Nonnull final Class<AbstractPersistable<Serializable>> type) {
		@Nonnull final var superclass = type.getSuperclass();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Object.class, superclass, "It is inheriting Object")
		);
	}

	@DisplayName("It is annotated")
	@Override
	public final void assertIsAnnotated(@Nonnull final Class<AbstractPersistable<Serializable>> type) {
		@Nonnull final var annotations = Arrays.stream(type.getDeclaredAnnotations()).map(Annotation::annotationType)
			.collect(Collectors.toUnmodifiableSet());
		@Nonnull final var expected = Set.of(MappedSuperclass.class);
		Assertions.assertEquals(expected, annotations, "It is annotated");
	}

	@DisplayName("It is not an annotation")
	@Override
	public final void assertIsNotAnnotation(@Nonnull final Class<AbstractPersistable<Serializable>> type) {
		Assertions.assertFalse(type.isAnnotation(), "It is not an annotation");
	}

	@DisplayName("It has an {Serializable pk} declared field")
	@Override
	public final void assertPKField(@Nonnull final Class<AbstractPersistable<Serializable>> type) throws NoSuchFieldException {
		@Nonnull final var field = type.getDeclaredField("pk");
		Assertions.assertAll("pk",
			() -> Assertions.assertEquals(PK_FIELD_VISIBILITY, field.getModifiers(), "{pk} Private visibility"),
			() -> Assertions.assertEquals(Serializable.class, field.getType(), "{pk} Serializable"),
			() -> MatcherAssert.assertThat("{pk} is annotated",
				Arrays.stream(field.getDeclaredAnnotations()).map(Annotation::annotationType)
					.collect(Collectors.toUnmodifiableSet()),
				Matchers.containsInAnyOrder(Column.class, GeneratedValue.class, Id.class)),
			() -> Assertions.assertFalse(field.getDeclaredAnnotation(Column.class).updatable(),
				"{pk} Is not updatable"),
			() -> Assertions.assertEquals(GenerationType.IDENTITY, field.getDeclaredAnnotation(GeneratedValue.class)
				.strategy(), "{pk} Generation strategy using a database identity column")
		);
	}

	@DisplayName("It has an {void setPk(Serializable)} declared method")
	@Override
	public final void assertSetPkMethod(@Nonnull final Class<AbstractPersistable<Serializable>> type) throws
		NoSuchMethodException {
		@Nonnull final var method = type.getMethod("setPk", Serializable.class);
		Assertions.assertAll("setPk(Serializable)",
			() -> Assertions.assertEquals(SET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{setPk(Serializable)} Public visibility"),
			() -> Assertions.assertEquals(1, method.getParameterCount(),
				"{setPk(Serializable)} With only one parameter"),
			() -> Assertions.assertEquals(Serializable.class, method.getParameters()[0].getType(),
				"{setPk(Serializable)} Serializable as first parameter"),
			() -> Assertions.assertNotNull(method.getParameters()[0].getDeclaredAnnotation(Nonnull.class),
				"{setPk(Serializable)} First parameter annotated with NotNull"),
			() -> Assertions.assertEquals(1, method.getParameters()[0].getDeclaredAnnotations().length,
				"{setPk(Serializable)} First parameter annotated only with NotNull"),
			() -> Assertions.assertEquals(Void.TYPE, method.getReturnType(), "{setPk(Serializable)} Void return")
		);
	}

	@DisplayName("It's a class")
	@Override
	public final void assertType(@Nonnull final Class<AbstractPersistable<Serializable>> type) {
		Assertions.assertFalse(type.isInterface(), "It's a class");
	}

	@DisplayName("It's abstract with public visibility")
	@Override
	public final void assertVisibility(@Nonnull final Class<AbstractPersistable<Serializable>> type) {
		Assertions.assertEquals(TYPE_VISIBILITY, type.getModifiers(), "It's abstract with public visibility");
	}
}
