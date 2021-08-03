package com.kinlhp.moname.commons.jpa;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.lang.Nullable;

/**
 * Architectural tests for abstraction of readable entities.
 */
class ReadableArchTests {

	private static final int GET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT | Modifier.INTERFACE;

	@DisplayName(value = "It's an interface.")
	@Test
	final void assertInterfaceType() {
		Assertions.assertTrue(Readable.class.isInterface(), "It's an interface.");
	}

	@DisplayName(value = "It's public visibility.")
	@Test
	final void assertPublicVisibility() {
		Assertions.assertEquals(TYPE_VISIBILITY, Readable.class.getModifiers(), "It's public visibility.");
	}

	@DisplayName(value = "It is inheriting only Serializable.")
	@Test
	final void assertInheritingOnlySerializable() {
		final var superclasses = Readable.class.getInterfaces();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Serializable.class, superclasses[0], "It is inheriting Serializable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is inheriting only Serializable."));
	}

	@DisplayName(value = "It is not an annotation.")
	@Test
	final void assertIsNotAnnotation() {
		Assertions.assertFalse(Readable.class.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It has an {Serializable getPk()} declared method.")
	@Test
	final void assertGetPkMethod() throws NoSuchMethodException {
		final var method = Readable.class.getDeclaredMethod("getPk");
		Assertions.assertAll("getPk()",
			() -> Assertions.assertEquals(GET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{getPk()} Public visibility."),
			() -> Assertions.assertEquals(0, method.getParameterCount(), "{getPk()} With no one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getReturnType(),
				"{getPk()} Returns a Serializable."),
			() -> Assertions.assertNotNull(method.getAnnotation(Nullable.class), "{getPk()} Annotated with Nullable."),
			() -> Assertions.assertEquals(1, method.getAnnotations().length,
				"{getPk()} Annotated only with Nullable."));
	}

}
