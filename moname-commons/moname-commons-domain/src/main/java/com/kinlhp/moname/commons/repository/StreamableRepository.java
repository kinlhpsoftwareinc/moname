package com.kinlhp.moname.commons.repository;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.domain.entity.Identifiable;
import com.kinlhp.moname.commons.util.SingletonCollector;

/**
 * {@inheritDoc}
 */
public interface StreamableRepository<T extends Identifiable<ID>, ID extends Serializable> extends IdentifiableRepository<T, ID>, Iterable<T> {

	/**
	 * @see org.springframework.data.util.Streamable#of(Supplier)
	 */
	@Nonnull
	static <T extends Identifiable<ID>, ID extends Serializable> StreamableRepository<T, ID> of(@Nonnull final Supplier<Stream<T>> supplier) {
		return new Of<>(supplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Nonnull
	@Override
	default Optional<T> one(@Nonnull final ID identity) {
		return filter(identifiable -> identifiable.is(identity)).stream().collect(SingletonCollector.toSingleton());
	}

	/**
	 * @see Iterable#iterator()
	 */
	@Nonnull
	default Iterator<T> all() {
		return iterator();
	}

	/**
	 * @see Stream#count()
	 */
	default long count() {
		return stream().count();
	}

	/**
	 * @see org.springframework.data.util.Streamable#stream()
	 */
	@Nonnull
	default Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	/**
	 * @see org.springframework.data.util.Streamable#filter(Predicate)
	 */
	@Nonnull
	default StreamableRepository<T, ID> filter(@Nonnull final Predicate<T> predicate) {
		return of(() -> stream().filter(predicate));
	}

	/**
	 * @see Stream#collect(Collector)
	 */
	@Nonnull
	default <R, A> R collect(@Nonnull final Collector<? super T, A, R> collector) {
		return stream().collect(collector);
	}

	/**
	 * @see org.springframework.data.util.LazyStreamable
	 */
	final class Of<T extends Identifiable<ID>, ID extends Serializable> implements StreamableRepository<T, ID> {

		@Nonnull
		private final Supplier<Stream<T>> supplier;

		private Of(@Nonnull final Supplier<Stream<T>> supplier) {
			this.supplier = supplier;
		}

		/**
		 * @see Iterable#iterator()
		 */
		@Nonnull
		@Override
		public Iterator<T> iterator() {
			return stream().iterator();
		}

		/**
		 * {@inheritDoc}
		 */
		@Nonnull
		@Override
		public Stream<T> stream() {
			return supplier.get();
		}
	}
}
