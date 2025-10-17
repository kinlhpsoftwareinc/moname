package com.kinlhp.moname.commons.api.openapi.mapper;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Slice;

public interface IterableFlatMapper<T, R> {

	@Nullable R map(final @Nullable T element);

	default <C extends Collection<R>> C map(final @Nullable Slice<T> slice, final Supplier<C> collectionFactory) {
		return Optional.ofNullable(slice)
				.filter(Slice::hasContent)
				.map(Slice::getContent)
				.stream()
				.flatMap(elements -> elements.stream().map(this::map))
				.collect(Collectors.toCollection(collectionFactory));
	}
}
