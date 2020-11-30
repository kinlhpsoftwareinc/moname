package com.kinlhp.moname.commons.jpa;

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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.lang.Nullable;

/**
 * Architectural tests for abstract implementation of persistable entities.
 */
class AbstractPersistableArchTests {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC;
	private static final int PK_FIELD_VISIBILITY = Modifier.PRIVATE;
	private static final int SET_PK_METHOD_VISIBILITY = Modifier.PUBLIC;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;

	@DisplayName(value = "It's an class.")
	@Test
	final void assertClassType() {
		Assertions.assertFalse(AbstractPersistable.class.isInterface(), "It's an class.");
	}

	@DisplayName(value = "It's abstract with public visibility.")
	@Test
	final void assertAbstractWithPublicVisibility() {
		Assertions.assertEquals(TYPE_VISIBILITY, AbstractPersistable.class.getModifiers(),
			"It's abstract with public visibility.");
	}

	@DisplayName(value = "It is implementing only Persistable.")
	@Test
	final void assertImplementingOnlyPersistable() {
		final var superclasses = AbstractPersistable.class.getInterfaces();
		Assertions.assertAll("implementing",
			() -> Assertions.assertEquals(Persistable.class, superclasses[0], "It is implementing Persistable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is implementing only Persistable."));
	}

	@DisplayName(value = "It is annotated.")
	@Test
	final void assertIsAnnotated() {
		final var annotations = Arrays.stream(AbstractPersistable.class.getDeclaredAnnotations())
			.map(Annotation::annotationType).collect(Collectors.toUnmodifiableSet());
		final var expected = Set.of(MappedSuperclass.class);
		Assertions.assertEquals(expected, annotations, "It is annotated.");
	}

	@DisplayName(value = "It is not an annotation.")
	@Test
	final void assertIsNotAnnotation() {
		Assertions.assertFalse(AbstractPersistable.class.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It has an {Serializable pk} declared field.")
	@Test
	final void assertPkField() throws NoSuchFieldException {
		final var field = AbstractPersistable.class.getDeclaredField("pk");
		Assertions.assertAll("pk",
			() -> Assertions.assertEquals(PK_FIELD_VISIBILITY, field.getModifiers(), "{pk} Private visibility."),
			() -> Assertions.assertEquals(Serializable.class, field.getType(), "{pk} Serializable."),
			() -> MatcherAssert.assertThat("{pk} is annotated.", Arrays.stream(field.getDeclaredAnnotations())
					.map(Annotation::annotationType).collect(Collectors.toUnmodifiableSet()),
				Matchers.containsInAnyOrder(Column.class, GeneratedValue.class, Id.class)),
			() -> Assertions.assertFalse(field.getDeclaredAnnotation(Column.class).updatable(),
				"{pk} Is not updatable."),
			() -> Assertions.assertEquals(GenerationType.IDENTITY, field.getDeclaredAnnotation(GeneratedValue.class)
				.strategy(), "{pk} Generation strategy using a database identity column."));
	}

	@DisplayName(value = "It has an {Serializable getPk()} method.")
	@Test
	final void assertGetPkMethod() throws NoSuchMethodException {
		final var method = AbstractPersistable.class.getDeclaredMethod("getPk");
		Assertions.assertAll("getPk()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPk()} Public visibility."),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPk()} With no one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPk()} Returns a Serializable."),
			() -> Assertions.assertNotNull(method.getAnnotation(Nullable.class), "{getPk()} Annotated with Nullable."),
			() -> Assertions.assertEquals(1, method.getAnnotations().length, "{getPk()} Annotated only with Nullable.")
		);
	}

	@DisplayName(value = "It has an {void setPk(Serializable)} declared method.")
	@Test
	final void assertSetPkMethod() throws NoSuchMethodException {
		final var method = AbstractPersistable.class.getDeclaredMethod("setPk", Serializable.class);
		Assertions.assertAll("setPk(Serializable)",
			() -> Assertions.assertEquals(SET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{setPk(Serializable)} Public visibility."),
			() -> Assertions.assertEquals(1, method.getParameterCount(),
				"{setPk(Serializable)} With only one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getParameters()[0].getType(),
				"{setPk(Serializable)} Serializable as first parameter."),
			() -> Assertions.assertNotNull(method.getParameters()[0].getDeclaredAnnotation(Nullable.class),
				"{setPk(Serializable)} First parameter annotated with Nullable."),
			() -> Assertions.assertEquals(1, method.getParameters()[0].getDeclaredAnnotations().length,
				"{setPk(Serializable)} First parameter annotated only with Nullable."),
			() -> Assertions.assertEquals(Void.TYPE, method.getReturnType(), "{setPk(Serializable)} Void return."));
	}

}
