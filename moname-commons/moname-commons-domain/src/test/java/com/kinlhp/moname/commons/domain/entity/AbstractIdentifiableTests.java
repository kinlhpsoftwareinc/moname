package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;

import jakarta.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class AbstractIdentifiableTests<T extends AbstractIdentifiable<T, Integer>> {

	@Test
	void identifiedBy() {
		@Nonnull final var identifiable = new AbstractIdentifiable<T, Integer>(Integer.MAX_VALUE) {

			@Serial
			private static final long serialVersionUID = -8860434686198577463L;
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

		@Nonnull final var exception = Assertions.assertThrows(UnsupportedOperationException.class,
				() -> uncloneable.copyIdentifiedBy(Integer.MIN_VALUE));

		Assertions.assertEquals(
				"The domain entity identified by 2147483647 cannot in any way be identified by -2147483648",
				exception.getLocalizedMessage()
		);
	}

	@SuppressWarnings("unchecked")
	@Test
	void equals() {
		@Nonnull final var parent = (T) new Parent(Integer.MAX_VALUE);
		@Nonnull final var firstLevelInheritance = (T) new FirstLevelInheritance(Integer.MAX_VALUE);
		@Nonnull final var secondLevelInheritance = (T) new SecondLevelInheritance(Integer.MAX_VALUE);

		@Nonnull final var anotherParent = (T) new Parent(Integer.MAX_VALUE);
		@Nonnull final var anotherFirstLevelInheritance = (T) new FirstLevelInheritance(Integer.MAX_VALUE);
		@Nonnull final var anotherSecondLevelInheritance = (T) new SecondLevelInheritance(Integer.MAX_VALUE);

		Assertions.assertEquals(parent, anotherParent);
		Assertions.assertEquals(firstLevelInheritance, anotherFirstLevelInheritance);
		Assertions.assertEquals(secondLevelInheritance, anotherSecondLevelInheritance);
	}

	@SuppressWarnings("unchecked")
	@Test
	void notEquals() {
		@Nonnull final var parent = (T) new Parent(Integer.MAX_VALUE);
		@Nonnull final var firstLevelInheritance = (T) new FirstLevelInheritance(Integer.MAX_VALUE);
		@Nonnull final var secondLevelInheritance = (T) new SecondLevelInheritance(Integer.MAX_VALUE);

		Assertions.assertNotEquals(parent, firstLevelInheritance);
		Assertions.assertNotEquals(parent, secondLevelInheritance);
		Assertions.assertNotEquals(firstLevelInheritance, secondLevelInheritance);

		Assertions.assertNotEquals(firstLevelInheritance, parent);
		Assertions.assertNotEquals(secondLevelInheritance, parent);
		Assertions.assertNotEquals(secondLevelInheritance, firstLevelInheritance);

		@Nonnull final var anotherParent = (T) new Parent(Integer.MIN_VALUE);
		@Nonnull final var anotherFirstLevelInheritance = (T) new FirstLevelInheritance(Integer.MIN_VALUE);
		@Nonnull final var anotherSecondLevelInheritance = (T) new SecondLevelInheritance(Integer.MIN_VALUE);

		Assertions.assertNotEquals(parent, anotherParent);
		Assertions.assertNotEquals(firstLevelInheritance, anotherFirstLevelInheritance);
		Assertions.assertNotEquals(secondLevelInheritance, anotherSecondLevelInheritance);
	}
}
