package com.kinlhp.moname.commons.util;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;

public final class SingletonCollector {

	private static final int EXPECTED_COUNT = 1;

	private SingletonCollector() {
	}

	/**
	 * @param <T> the type of input elements to the reduction operation.
	 * @return an {@link Optional<T>} with a present value if the value of the
	 * singleton element of this stream is non-{@code null}, otherwise an empty
	 * {@link Optional#EMPTY}.
	 * @throws IllegalStateException if this steam is composed of more than one
	 *                               element.
	 */
	@Nonnull
	public static <T> Collector<T, ?, Optional<T>> toSingleton() {
		@Nonnull final var counter = new AtomicInteger(EXPECTED_COUNT);
		return Collectors.collectingAndThen(countableDownstream(counter), countedFinisher(counter));
	}

	@Nonnull
	private static <T> Collector<T, ?, Optional<T>> countableDownstream(final @Nonnull AtomicInteger counter) {
		return Collectors.reducing((left, right) -> {
			counter.incrementAndGet();
			return left;
		});
	}

	@Nonnull
	private static <T> Function<Optional<T>, Optional<T>> countedFinisher(final @Nonnull AtomicInteger counter) {
		return element -> {
			ensure(counter.get());
			return element;
		};
	}

	private static void ensure(final int count) {
		if (count > 1) {
			throw new IllegalStateException(exceptionMessageOf(count));
		}
	}

	@Nonnull
	private static String exceptionMessageOf(final int count) {
		final var howManyMore = count - EXPECTED_COUNT;
		@Nonnull final var pastTense = howManyMore == 1 ? "was" : "were";
		return String.format("A single element is expected, but %d more %s found.", howManyMore, pastTense);
	}
}
