package com.kinlhp.moname.commons.jpa;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.lang.Nullable;

/**
 * Architectural tests for abstract implementation of read-only entities.
 */
class AbstractReadOnlyArchTests {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC;
	private static final int PK_FIELD_VISIBILITY = Modifier.PRIVATE;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;

	@DisplayName(value = "It's an class.")
	@Test
	final void assertClassType() {
		Assertions.assertFalse(AbstractReadOnly.class.isInterface(), "It's an class.");
	}

	@DisplayName(value = "It's abstract with public visibility.")
	@Test
	final void assertAbstractWithPublicVisibility() {
		Assertions.assertEquals(TYPE_VISIBILITY, AbstractReadOnly.class.getModifiers(),
			"It's abstract with public visibility.");
	}

	@DisplayName(value = "It is implementing only Readable.")
	@Test
	final void assertImplementingOnlyReadable() {
		final var superclasses = AbstractReadOnly.class.getInterfaces();
		Assertions.assertAll("implementing",
			() -> Assertions.assertEquals(Readable.class, superclasses[0], "It is implementing Readable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is implementing only Readable."));
	}

	@DisplayName(value = "It is annotated.")
	@Test
	final void assertIsAnnotated() {
		MatcherAssert.assertThat("It is annotated.", Arrays.stream(AbstractReadOnly.class.getDeclaredAnnotations())
				.map(Annotation::annotationType).collect(Collectors.toUnmodifiableSet()),
			Matchers.containsInAnyOrder(EntityListeners.class, MappedSuperclass.class));
	}

	@DisplayName(value = "It is not an annotation.")
	@Test
	final void assertIsNotAnnotation() {
		Assertions.assertFalse(AbstractReadOnly.class.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It is listened only from ReadOnlyListener.")
	@Test
	final void assertReadOnlyListener() {
		final var callbacks = AbstractReadOnly.class.getDeclaredAnnotation(EntityListeners.class).value();
		Assertions.assertAll("callbacks",
			() -> Assertions.assertEquals(ReadOnlyListener.class, callbacks[0],
				"It is listened from ReadOnlyListener."),
			() -> Assertions.assertEquals(1, callbacks.length, "It is listened only from ReadOnlyListener."));
	}

	@DisplayName(value = "It has an {Serializable pk} declared field.")
	@Test
	final void assertPkField() throws NoSuchFieldException {
		final var field = AbstractReadOnly.class.getDeclaredField("pk");
		Assertions.assertAll("pk",
			() -> Assertions.assertEquals(PK_FIELD_VISIBILITY, field.getModifiers(), "{pk} Private visibility."),
			() -> Assertions.assertEquals(Serializable.class, field.getType(), "{pk} Serializable."),
			() -> Assertions.assertNotNull(field.getDeclaredAnnotationsByType(Id.class), "{pk} Id annotation."),
			() -> Assertions.assertEquals(1, field.getDeclaredAnnotations().length, "{pk} Id annotation only."));
	}

	@DisplayName(value = "It has an {Serializable getPk()} method.")
	@Test
	final void assertGetPkMethod() throws NoSuchMethodException {
		final var method = AbstractReadOnly.class.getDeclaredMethod("getPk");
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

}
