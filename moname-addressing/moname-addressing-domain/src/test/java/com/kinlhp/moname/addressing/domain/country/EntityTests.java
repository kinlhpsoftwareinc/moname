package com.kinlhp.moname.addressing.domain.country;

import jakarta.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EntityTests {

	@Test
	final void getIdentity() {
		@Nonnull final var identity = Entity.BRA.getIdentity();

		Assertions.assertEquals("BRA", identity);
	}

	@Test
	final void of() {
		@Nonnull final var one = Entity.of(76);
		@Nonnull final var another = Entity.of("076");

		Assertions.assertEquals(Entity.BRA, one);
		Assertions.assertEquals(Entity.BRA, another);
	}

	@Test
	final void ofNonexistent() {
		final var oneException = Assertions.assertThrows(IllegalArgumentException.class, () -> Entity.of(893));
		final var anotherException = Assertions.assertThrows(IllegalArgumentException.class, () -> Entity.of("893"));

		Assertions.assertEquals("There are no one country identified by numeric code 893.", oneException.getLocalizedMessage());
		Assertions.assertEquals("There are no one country identified by numeric code 893.", anotherException.getLocalizedMessage());
	}

	@Test
	final void ofNull() {
		final var anotherException = Assertions.assertThrows(IllegalArgumentException.class, () -> Entity.of(null));

		Assertions.assertEquals("There are no one country identified by numeric code null.", anotherException.getLocalizedMessage());
	}

	@Test
	final void numericCodeOf() {
		@Nonnull final var numericCode = Entity.numericCodeOf(4);

		Assertions.assertEquals("004", numericCode);
	}
}
