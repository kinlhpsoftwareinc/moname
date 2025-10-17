package com.kinlhp.moname.commons.api.openapi.mapper;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.springframework.data.domain.Slice;

public interface IterableFlatMapper<T, R> {

	@Nullable
	R map(@Nullable final T element);

	@Nonnull
	default <C extends Collection<R>> C map(@Nullable final Slice<T> slice, @Nonnull final Supplier<C> collectionFactory) {
		return Optional.ofNullable(slice)
				.filter(Slice::hasContent)
				.map(Slice::getContent)
				.stream()
				.flatMap(elements -> elements.stream().map(this::map))
				.collect(Collectors.toCollection(collectionFactory));
	}
}
