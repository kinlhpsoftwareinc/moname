//package com.kinlhp.moname.commons.repository;
//
//import java.io.Serializable;
//import java.util.Iterator;
//import java.util.Optional;
//import java.util.function.Predicate;
//import java.util.function.Supplier;
//import java.util.stream.Collector;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//import jakarta.annotation.Nonnull;
//
//import com.kinlhp.moname.commons.domain.entity.Identifiable;
//import com.kinlhp.moname.commons.util.SingletonCollector;
//
///**
// * @param <T>  the entity's type.
// * @param <ID> the entity's identity (ID) type.
// */
//public interface IdentifiableRepository_Bkp<T extends Identifiable<ID>, ID extends Serializable> extends Iterable<T> {
//
//	// TODO: https://softwareengineering.stackexchange.com/a/396159
//
//	@Nonnull
//	static <T extends Identifiable<ID>, ID extends Serializable> IdentifiableRepository_Bkp<T, ID> of(@Nonnull final Supplier<Stream<T>> supplier) {
//		return StreamableRepository.of(supplier);
//	}
//
//	@Nonnull
//	default Iterator<T> all() {
//		return iterator();
//	}
//
//	default long count() {
//		return stream().count();
//	}
//
//	@Nonnull
//	default Optional<T> ofIdentity(@Nonnull final ID identity) {
//		return stream().filter(identifiable -> identifiable.is(identity)).collect(SingletonCollector.toSingleton());
//	}
//
//	/**
//	 * Returns a new {@link IdentifiableRepository_Bkp} that will apply the given filter {@link Predicate} to the current one.
//	 *
//	 * @param predicate must not be {@code null}.
//	 * @return a new {@link IdentifiableRepository_Bkp} which will have the given filter applied.
//	 * @see Stream#filter(Predicate)
//	 */
//	@Nonnull
//	default IdentifiableRepository_Bkp<T, ID> filter(@Nonnull final Predicate<T> predicate) {
//		return of(() -> stream().filter(predicate));
//	}
//
//	/**
//	 * Creates a non-parallel {@link Stream<T>} of the underlying {@link Iterable<T>}.
//	 *
//	 * @return will never be {@code null}.
//	 * @deprecated No replacement. Implement desired instead. See {@link #collect(Collector)}.
//	 */
//	@Deprecated(since = "1.0.0")
//	@Nonnull
//	default Stream<T> stream() {
//		return StreamSupport.stream(spliterator(), false);
//	}
//
//	/**
//	 * See {@link Stream#collect(Collector)}.
//	 */
//	@Nonnull
//	default <R, A> R collect(@Nonnull final Collector<? super T, A, R> collector) {
//		return stream().collect(collector);
//	}
//}
