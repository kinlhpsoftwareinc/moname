package com.kinlhp.moname.commons.jpa.arch;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.kinlhp.moname.commons.jpa.AbstractPersistable;
import com.kinlhp.moname.commons.jpa.Persistable;
import com.kinlhp.moname.commons.jpa.arch.AbstractPersistableArchTests;

/**
 * Architectural tests for abstract implementation of persistable entities.
 */
class AbstractPersistableTests implements
	AbstractPersistableArchTests<AbstractPersistable<Serializable>, Serializable> {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC;
	private static final int PK_FIELD_VISIBILITY = Modifier.PRIVATE;
	private static final int SET_PK_METHOD_VISIBILITY = Modifier.PUBLIC;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;

	@Override
	@ParameterizedTest
	@ValueSource(classes = {AbstractPersistable.class})
	public final void assertAbstractPersistableArch(final Class<AbstractPersistable<Serializable>> clazz) throws
		NoSuchMethodException {
		AbstractPersistableArchTests.super.assertAbstractPersistableArch(clazz);
	}

	@DisplayName(value = "It has an {Serializable getPk()} method.")
	@Override
	public final void assertGetPkMethod(final Class<AbstractPersistable<Serializable>> clazz) throws
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

	@DisplayName(value = "It is implementing only Persistable.")
	@Override
	public final void assertImplements(final Class<AbstractPersistable<Serializable>> clazz) {
		final var superclasses = clazz.getInterfaces();
		Assertions.assertAll("implementing",
			() -> Assertions.assertEquals(Persistable.class, superclasses[0], "It is implementing Persistable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is implementing only Persistable.")
		);
	}

	@DisplayName(value = "It is inheriting Object.")
	@Override
	public final void assertInherits(final Class<AbstractPersistable<Serializable>> clazz) {
		final var superclass = clazz.getSuperclass();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Object.class, superclass, "It is inheriting Object.")
		);
	}

	@DisplayName(value = "It is annotated.")
	@Override
	public final void assertIsAnnotated(final Class<AbstractPersistable<Serializable>> clazz) {
		final var annotations = Arrays.stream(clazz.getDeclaredAnnotations()).map(Annotation::annotationType)
			.collect(Collectors.toUnmodifiableSet());
		final var expected = Set.of(MappedSuperclass.class);
		Assertions.assertEquals(expected, annotations, "It is annotated.");
	}

	@DisplayName(value = "It is not an annotation.")
	@Override
	public final void assertIsNotAnnotation(final Class<AbstractPersistable<Serializable>> clazz) {
		Assertions.assertFalse(clazz.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It has an {Serializable pk} declared field.")
	@Override
	public final void assertPkField(final Class<AbstractPersistable<Serializable>> clazz) throws NoSuchFieldException {
		final var field = clazz.getDeclaredField("pk");
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

	@DisplayName(value = "It has an {void setPk(Serializable)} declared method.")
	@Override
	public final void assertSetPkMethod(final Class<AbstractPersistable<Serializable>> clazz) throws
		NoSuchMethodException {
		final var method = clazz.getDeclaredMethod("setPk", Serializable.class);
		Assertions.assertAll("setPk(Serializable)",
			() -> Assertions.assertEquals(SET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{setPk(Serializable)} Public visibility."),
			() -> Assertions.assertEquals(1, method.getParameterCount(),
				"{setPk(Serializable)} With only one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getParameters()[0].getType(),
				"{setPk(Serializable)} Serializable as first parameter."),
			() -> Assertions.assertNotNull(method.getParameters()[0].getDeclaredAnnotation(NotNull.class),
				"{setPk(Serializable)} First parameter annotated with NotNull."),
			() -> Assertions.assertEquals(1, method.getParameters()[0].getDeclaredAnnotations().length,
				"{setPk(Serializable)} First parameter annotated only with NotNull."),
			() -> Assertions.assertEquals(Void.TYPE, method.getReturnType(), "{setPk(Serializable)} Void return.")
		);
	}

	@DisplayName(value = "It's an class.")
	@Override
	public final void assertType(final Class<AbstractPersistable<Serializable>> clazz) {
		Assertions.assertFalse(clazz.isInterface(), "It's an class.");
	}

	@DisplayName(value = "It's abstract with public visibility.")
	@Override
	public final void assertVisibility(final Class<AbstractPersistable<Serializable>> clazz) {
		Assertions.assertEquals(TYPE_VISIBILITY, clazz.getModifiers(), "It's abstract with public visibility.");
	}

}
