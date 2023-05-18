package com.kinlhp.moname.commons.util;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.function.ThrowingSupplier;

import com.kinlhp.moname.commons.domain.entity.Immutable;

final class SingletonCollectorTests {

	@Test
	void foundNone() {
		@Nonnull final var none = Assertions.assertDoesNotThrow(noneSupplier());
		Assertions.assertAll(
			() -> Assertions.assertNotNull(none),
			() -> Assertions.assertTrue(none.isEmpty())
		);
	}

	@Test
	void foundOne() {
		@Nonnull final var one = Assertions.assertDoesNotThrow(oneSupplier(Integer.MAX_VALUE));
		Assertions.assertAll(
			() -> Assertions.assertNotNull(one),
			() -> Assertions.assertEquals(Integer.MAX_VALUE, one.orElseThrow())
		);
	}

	@Test
	void foundOneEmpty() {
		@Nonnull final var empty = Assertions.assertDoesNotThrow(oneSupplier(null));
		Assertions.assertAll(
			() -> Assertions.assertNotNull(empty),
			() -> Assertions.assertTrue(empty.isEmpty())
		);
	}

	@Test
	void foundOneMore() {
		@Nonnull final var exception = Assertions.assertThrows(IllegalStateException.class, oneMoreSupplier());
		Assertions.assertEquals("A single element is expected, but 1 more was found.", exception.getLocalizedMessage());
	}

	@Test
	void foundManyMore() {
		@Nonnull final var exception = Assertions.assertThrows(IllegalStateException.class, manyMoreSupplier());
		Assertions.assertEquals("A single element is expected, but 9 more were found.", exception.getLocalizedMessage());
	}

	@Nonnull
	private <T> ThrowingSupplier<Optional<T>> noneSupplier() {
		//noinspection unchecked
		return () -> (Optional<T>) Stream.empty().collect(SingletonCollector.toSingleton());
	}

	@Nonnull
	private <T> ThrowingSupplier<Optional<T>> oneSupplier(@Nullable final T element) {
		return () -> Stream.of(element).collect(SingletonCollector.toSingleton());
	}

	@Nonnull
	private Executable oneMoreSupplier() {
		//noinspection ResultOfMethodCallIgnored
		return () -> IntStream.range(0, 2)
			.mapToObj(Immutable::new)
			.collect(SingletonCollector.toSingleton());
	}

	@Nonnull
	private Executable manyMoreSupplier() {
		//noinspection ResultOfMethodCallIgnored
		return () -> IntStream.range(0, 10)
			.mapToObj(Immutable::new)
			.collect(SingletonCollector.toSingleton());
	}
}
