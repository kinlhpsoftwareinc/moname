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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kinlhp.moname.commons.domain.entity.Identifiable;
import com.kinlhp.moname.commons.stream.SingletonCollector;

/**
 * {@inheritDoc}
 */
@SuppressWarnings("java:S119")
public interface StreamableRepository<T extends Identifiable<ID>, ID extends Serializable>
		extends IdentifiableRepository<T, ID>, Iterable<T> {

	@Nonnull
	Logger LOG = LoggerFactory.getLogger(StreamableRepository.class);

	/**
	 * @see org.springframework.data.util.Streamable#of(Supplier) of
	 */
	@Nonnull
	static <T extends Identifiable<ID>, ID extends Serializable> StreamableRepository<T, ID> of(
			@Nonnull final Supplier<Stream<T>> supplier) {
		return new Of<>(supplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Nonnull
	@Override
	default Optional<T> one(@Nonnull final ID identity) {
		LOG.debug("Searching single identified by {}", identity);
		return filter(identifiable -> identifiable.is(identity)).stream().collect(SingletonCollector.toSingle());
	}

	/**
	 * @see Iterable#iterator() iterator
	 */
	@Nonnull
	default StreamableRepository<T, ID> all() {
		LOG.debug("Getting without filter");
		return filter(unused -> Boolean.TRUE);
	}

	/**
	 * @see Stream#count() count
	 */
	default long count() {
		LOG.debug("Counting");
		return stream().count();
	}

	/**
	 * @see org.springframework.data.util.Streamable#stream() stream
	 */
	@Nonnull
	default Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	/**
	 * @see org.springframework.data.util.Streamable#filter(Predicate) filter
	 */
	@Nonnull
	default StreamableRepository<T, ID> filter(@Nonnull final Predicate<T> predicate) {
		LOG.debug("Searching for filter matches");
		return of(() -> stream().filter(predicate));
	}

	/**
	 * @see Stream#collect(Collector) collect
	 */
	@Nonnull
	default <R, A> R collect(@Nonnull final Collector<? super T, A, R> collector) {
		return stream().collect(collector);
	}

	/**
	 * @see org.springframework.data.util.LazyStreamable LazyStreamable
	 */
	record Of<T extends Identifiable<ID>, ID extends Serializable>(@Nonnull Supplier<Stream<T>> supplier)
			implements StreamableRepository<T, ID> {

		@Nonnull
		private static final Logger LOG = LoggerFactory.getLogger(Of.class);

		public Of {
			LOG.trace("Constructing a new streamable");
		}

		/**
		 * @see Iterable#iterator() iterator
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
