package com.kinlhp.moname.commons.domain.entity;

import jakarta.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class AbstractIdentifiableTests {

	@Test
	<T extends AbstractIdentifiable<T, Integer>> void identifiedBy() {
		@Nonnull @SuppressWarnings(value = "serial") final var identifiable = new AbstractIdentifiable<T, Integer>(Integer.MAX_VALUE) {
		};

		Assertions.assertEquals(Integer.MAX_VALUE, identifiable.getIdentity().intValue());
	}

	@Test
	void copyIdentifiedBy() {
		@Nonnull final var immutable = new Immutable(Integer.MAX_VALUE);
		@Nonnull final var copy = immutable.copyIdentifiedBy(Integer.MIN_VALUE);

		Assertions.assertNotEquals(immutable, copy);
		Assertions.assertNotEquals(copy, immutable);
		Assertions.assertEquals(immutable.property, copy.property);
	}

	@Test
	void uncloneable() {
		@Nonnull final var uncloneable = new Uncloneable(Integer.MAX_VALUE);

		@Nonnull final var exception = Assertions.assertThrows(UnsupportedOperationException.class, () -> uncloneable.copyIdentifiedBy(Integer.MIN_VALUE));
		Assertions.assertEquals("The domain entity identified by 2147483647 cannot in any way be identified by -2147483648.", exception.getLocalizedMessage());
	}

	@Test
	void testEquals() {
		@Nonnull final AbstractIdentifiable<?, ?> parent = new Parent(Integer.MAX_VALUE);
		@Nonnull final AbstractIdentifiable<?, ?> firstLevelInheritance = new FirstLevelInheritance(Integer.MAX_VALUE);
		@Nonnull final AbstractIdentifiable<?, ?> secondLevelInheritance = new SecondLevelInheritance(Integer.MAX_VALUE);

		@Nonnull final AbstractIdentifiable<?, ?> anotherParent = new Parent(Integer.MAX_VALUE);
		@Nonnull final AbstractIdentifiable<?, ?> anotherFirstLevelInheritance = new FirstLevelInheritance(Integer.MAX_VALUE);
		@Nonnull final AbstractIdentifiable<?, ?> anotherSecondLevelInheritance = new SecondLevelInheritance(Integer.MAX_VALUE);

		Assertions.assertEquals(parent, anotherParent);
		Assertions.assertEquals(firstLevelInheritance, anotherFirstLevelInheritance);
		Assertions.assertEquals(secondLevelInheritance, anotherSecondLevelInheritance);
	}

	@Test
	void notEquals() {
		@Nonnull final AbstractIdentifiable<?, ?> parent = new Parent(Integer.MAX_VALUE);
		@Nonnull final AbstractIdentifiable<?, ?> firstLevelInheritance = new FirstLevelInheritance(Integer.MAX_VALUE);
		@Nonnull final AbstractIdentifiable<?, ?> secondLevelInheritance = new SecondLevelInheritance(Integer.MAX_VALUE);

		Assertions.assertNotEquals(parent, firstLevelInheritance);
		Assertions.assertNotEquals(parent, secondLevelInheritance);
		Assertions.assertNotEquals(firstLevelInheritance, secondLevelInheritance);

		Assertions.assertNotEquals(firstLevelInheritance, parent);
		Assertions.assertNotEquals(secondLevelInheritance, parent);
		Assertions.assertNotEquals(secondLevelInheritance, firstLevelInheritance);

		@Nonnull final AbstractIdentifiable<?, ?> anotherParent = new Parent(Integer.MIN_VALUE);
		@Nonnull final AbstractIdentifiable<?, ?> anotherFirstLevelInheritance = new FirstLevelInheritance(Integer.MIN_VALUE);
		@Nonnull final AbstractIdentifiable<?, ?> anotherSecondLevelInheritance = new SecondLevelInheritance(Integer.MIN_VALUE);

		Assertions.assertNotEquals(parent, anotherParent);
		Assertions.assertNotEquals(firstLevelInheritance, anotherFirstLevelInheritance);
		Assertions.assertNotEquals(secondLevelInheritance, anotherSecondLevelInheritance);
	}
}
