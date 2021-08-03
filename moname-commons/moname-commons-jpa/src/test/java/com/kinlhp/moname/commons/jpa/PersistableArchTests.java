package com.kinlhp.moname.commons.jpa;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.lang.Nullable;

/**
 * Architectural tests for abstraction of persistable entities.
 */
class PersistableArchTests {

	private static final int SET_PK_METHOD_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT;
	private static final int TYPE_VISIBILITY = Modifier.PUBLIC | Modifier.ABSTRACT | Modifier.INTERFACE;

	@DisplayName(value = "It's an interface.")
	@Test
	final void assertInterfaceType() {
		Assertions.assertTrue(Persistable.class.isInterface(), "It's an interface.");
	}

	@DisplayName(value = "It's public visibility.")
	@Test
	final void assertPublicVisibility() {
		Assertions.assertEquals(TYPE_VISIBILITY, Persistable.class.getModifiers(), "It's public visibility.");
	}

	@DisplayName(value = "It is inheriting only Readable.")
	@Test
	final void assertInheritingOnlyReadable() {
		final var superclasses = Persistable.class.getInterfaces();
		Assertions.assertAll("inheriting",
			() -> Assertions.assertEquals(Readable.class, superclasses[0], "It is inheriting Readable."),
			() -> Assertions.assertEquals(1, superclasses.length, "It is inheriting only Readable."));
	}

	@DisplayName(value = "It is not an annotation.")
	@Test
	final void assertIsNotAnnotation() {
		Assertions.assertFalse(Persistable.class.isAnnotation(), "It is not an annotation.");
	}

	@DisplayName(value = "It has an {void setPk(Serializable)} declared method.")
	@Test
	final void assertSetPkMethod() throws NoSuchMethodException {
		final var method = Persistable.class.getDeclaredMethod("setPk", Serializable.class);
		Assertions.assertAll("setPk(Serializable)",
			() -> Assertions.assertEquals(SET_PK_METHOD_VISIBILITY, method.getModifiers(),
				"{setPk(Serializable)} Public visibility."),
			() -> Assertions.assertEquals(1, method.getParameterCount(),
				"{setPk(Serializable)} With only one parameter."),
			() -> Assertions.assertEquals(Serializable.class, method.getParameters()[0].getType(),
				"{setPk(Serializable)} Serializable as first parameter."),
			() -> Assertions.assertEquals(Modifier.FINAL, (method.getParameters()[0].getModifiers() | Modifier.FINAL),
				"{setPk(Serializable)} First parameter is final."),
			() -> Assertions.assertNotNull(method.getParameters()[0].getDeclaredAnnotation(Nullable.class),
				"{setPk(Serializable)} First parameter annotated with Nullable."),
			() -> Assertions.assertEquals(1, method.getParameters()[0].getDeclaredAnnotations().length,
				"{setPk(Serializable)} First parameter annotated only with Nullable."),
			() -> Assertions.assertEquals(Void.TYPE, method.getReturnType(), "{setPk(Serializable)} Void return."));
	}

}
