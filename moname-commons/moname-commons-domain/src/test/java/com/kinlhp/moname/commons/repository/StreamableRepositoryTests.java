package com.kinlhp.moname.commons.repository;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import jakarta.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kinlhp.moname.commons.domain.entity.Identifiable;
import com.kinlhp.moname.commons.domain.entity.Immutable;
import com.kinlhp.moname.commons.util.SingletonCollector;

final class StreamableRepositoryTests<T extends Identifiable<Integer>> {

	@Nonnull
	private final StreamableRepository<T, Integer> repository = StreamableRepository.of(this::supplierOf);

	@Nonnull
	private Stream<T> supplierOf() {
		//noinspection unchecked
		return IntStream.range(0, 10)
			.mapToObj(identity -> (T) new Immutable(identity));
	}

	@Test
	void of() {
		Assertions.assertNotNull(repository);
	}

	@Test
	void one() {
		@Nonnull final var result = Assertions.assertDoesNotThrow(() -> repository.one(0));
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	void all() {
		Assertions.assertNotNull(repository.all());
	}

	@Test
	void count() {
		Assertions.assertEquals(10, repository.count());
	}

	@Test
	void stream() {
		Assertions.assertNotNull(repository.stream());
	}

	@Test
	void filter() {
		@Nonnull final var result = repository.filter(element -> element.is(0));
		Assertions.assertEquals(1, result.count());
	}

	@Test
	void collect() {
		@Nonnull final var collector = SingletonCollector.toSingleton();
		Assertions.assertAll(
			() -> Assertions.assertThrows(IllegalStateException.class, () -> repository.collect(collector)),
			() -> Assertions.assertDoesNotThrow(() -> repository.collect(Collectors.toUnmodifiableList()))
		);
	}
}
