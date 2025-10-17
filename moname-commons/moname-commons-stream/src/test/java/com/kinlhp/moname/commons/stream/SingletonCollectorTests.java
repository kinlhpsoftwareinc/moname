package com.kinlhp.moname.commons.stream;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

final class SingletonCollectorTests {

	@Test
	void none() {
		@Nonnull final var none = Assertions.assertDoesNotThrow(suppliesNone());
		Assertions.assertAll(
				() -> Assertions.assertNotNull(none),
				() -> Assertions.assertTrue(none.isEmpty())
		);
	}

	@Test
	void single() {
		@Nonnull final var single = Assertions.assertDoesNotThrow(suppliesSingle(Integer.MAX_VALUE));
		Assertions.assertAll(
				() -> Assertions.assertNotNull(single),
				() -> Assertions.assertEquals(Integer.MAX_VALUE, single.orElseThrow())
		);
	}

	@Test
	void singleEmpty() {
		@Nonnull final var empty = Assertions.assertDoesNotThrow(suppliesSingle(null));
		Assertions.assertAll(
				() -> Assertions.assertNotNull(empty),
				() -> Assertions.assertTrue(empty.isEmpty())
		);
	}

	@Test
	void oneMore() {
		@Nonnull final var exception = Assertions.assertThrows(IllegalStateException.class, this::suppliesOneMore);
		Assertions.assertEquals("A single element is expected, but 1 more was found",
				exception.getLocalizedMessage());
	}

	@Test
	void manyMore() {
		@Nonnull final var exception = Assertions.assertThrows(IllegalStateException.class, this::suppliesManyMore);
		Assertions.assertEquals("A single element is expected, but 9 more were found",
				exception.getLocalizedMessage());
	}

	@Nonnull
	private <T> ThrowingSupplier<Optional<T>> suppliesNone() {
		//noinspection unchecked
		return () -> (Optional<T>) Stream.empty().collect(SingletonCollector.toSingle());
	}

	@Nonnull
	private <T> ThrowingSupplier<Optional<T>> suppliesSingle(@Nullable final T element) {
		return () -> Stream.of(element).collect(SingletonCollector.toSingle());
	}

	private void suppliesOneMore() {
		//noinspection ResultOfMethodCallIgnored
		IntStream.range(0, 2)
				.boxed()
				.collect(SingletonCollector.toSingle());
	}

	private void suppliesManyMore() {
		//noinspection ResultOfMethodCallIgnored
		IntStream.range(0, 10)
				.boxed()
				.collect(SingletonCollector.toSingle());
	}
}
