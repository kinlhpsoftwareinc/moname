//package com.kinlhp.moname.commons.repository;
//
//import java.util.stream.IntStream;
//import java.util.stream.Stream;
//
//import jakarta.annotation.Nonnull;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import com.kinlhp.moname.commons.domain.entity.Identifiable;
//import com.kinlhp.moname.commons.domain.entity.Immutable;
//import com.kinlhp.moname.commons.util.SingletonCollector;
//
//final class IdentifiableRepositoryTests<T extends Identifiable<Integer>> {
//
//	@Nonnull
//	private final IdentifiableRepository<T, Integer> repository = IdentifiableRepository.of(this::data);
//
//	@Nonnull
//	private Stream<T> data() {
//		//noinspection unchecked
//		return IntStream.range(0, 10)
//			.mapToObj(identity -> (T) new Immutable(identity));
//	}
//
//	@Test
//	void of() {
//		@Nonnull final var repository = IdentifiableRepository.of(this::data);
//
//		Assertions.assertNotNull(repository);
//	}
//
//	@Test
//	void all() {
//		@Nonnull final var all = repository.all();
//
//		Assertions.assertNotNull(all);
//	}
//
//	@Test
//	void count() {
//		final var count = repository.count();
//
//		Assertions.assertEquals(10, count);
//	}
//
//	@Test
//	void ofIdentity() {
//		@Nonnull final var entity = repository.ofIdentity(0);
//
//		Assertions.assertAll(
//			() -> Assertions.assertNotNull(entity),
//			() -> Assertions.assertTrue(entity.isPresent())
//		);
//	}
//
//	@Test
//	void filter() {
//		@Nonnull final var found = repository.filter(entity -> entity.is(0));
//
//		Assertions.assertAll(
//			() -> Assertions.assertNotNull(found),
//			() -> Assertions.assertTrue(found.collect(SingletonCollector.toSingleton()).isPresent())
//		);
//	}
//
//	@Test
//	void stream() {
//		@Nonnull final var stream = repository.stream();
//
//		Assertions.assertNotNull(stream);
//	}
//}
