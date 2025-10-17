package com.kinlhp.moname.commons.stream;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SingletonCollector {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(SingletonCollector.class);

	private static final int EXPECTED_COUNT = 1;

	private SingletonCollector() {
	}

	/**
	 * @param <T> the type of input elements to the reduction operation.
	 * @return an {@link Optional Optional} with a present value if the value of the single element of this stream is
	 * non-{@code null}, otherwise an empty ({@link Optional#empty() empty}).
	 * @throws IllegalStateException if this stream is composed of more than one element.
	 */
	@Nonnull
	public static <T> Collector<T, ?, Optional<T>> toSingle() {
		@Nonnull final var counter = new AtomicInteger(EXPECTED_COUNT);
		return Collectors.collectingAndThen(countableDownstream(counter), countedFinisher(counter));
	}

	@Nonnull
	private static <T> Collector<T, ?, Optional<T>> countableDownstream(@Nonnull final AtomicInteger counter) {
		LOG.debug("Reducing it to just one");
		return Collectors.reducing((left, right) -> {
			LOG.trace("Counting");
			counter.incrementAndGet();
			return left;
		});
	}

	@Nonnull
	private static <T> Function<Optional<T>, Optional<T>> countedFinisher(@Nonnull final AtomicInteger counter) {
		return element -> {
			LOG.trace("Summarizing count");
			ensure(counter.get());
			return element;
		};
	}

	private static void ensure(final int count) {
		LOG.trace("Ensuring count");
		if (count > 1) {
			@Nonnull final var message = exceptionMessageOf(count);
			LOG.error(message);
			throw new IllegalStateException(message);
		}
	}

	@Nonnull
	private static String exceptionMessageOf(final int count) {
		final var howManyMore = count - EXPECTED_COUNT;
		@Nonnull final var pastTense = howManyMore == 1 ? "was" : "were";
		return "A single element is expected, but %d more %s found".formatted(howManyMore, pastTense);
	}
}
