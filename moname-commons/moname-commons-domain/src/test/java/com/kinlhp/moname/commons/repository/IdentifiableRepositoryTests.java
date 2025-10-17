package com.kinlhp.moname.commons.repository;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import jakarta.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kinlhp.moname.commons.domain.entity.Identifiable;
import com.kinlhp.moname.commons.domain.entity.Immutable;
import com.kinlhp.moname.commons.stream.SingletonCollector;

final class IdentifiableRepositoryTests<T extends Identifiable<Integer>> {

	@Nonnull
	private final IdentifiableRepository<T, Integer> repository = new Of<>(this::supplierOf);

	@Nonnull
	private Stream<T> supplierOf() {
		//noinspection unchecked
		return IntStream.range(0, 10).mapToObj(identity -> (T) new Immutable(identity));
	}

	@Test
	void one() {
		@Nonnull final var result = Assertions.assertDoesNotThrow(() -> repository.one(() -> 0));
		Assertions.assertTrue(result.isPresent());
	}

	@SuppressWarnings("java:S119")
	private record Of<T extends Identifiable<ID>, ID extends Serializable>(@Nonnull Supplier<Stream<T>> supplier)
			implements IdentifiableRepository<T, ID> {

		@Nonnull
		@Override
		public Optional<T> one(@Nonnull final ID identity) {
			return stream().filter(identifiable -> identifiable.is(identity)).collect(SingletonCollector.toSingle());
		}

		@Nonnull
		private Stream<T> stream() {
			return supplier.get();
		}
	}
}
