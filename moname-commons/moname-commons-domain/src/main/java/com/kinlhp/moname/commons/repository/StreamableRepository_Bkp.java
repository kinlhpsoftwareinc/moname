//package com.kinlhp.moname.commons.repository;
//
//import java.io.Serializable;
//import java.util.Iterator;
//import java.util.function.Supplier;
//import java.util.stream.Stream;
//
//import jakarta.annotation.Nonnull;
//
//import com.kinlhp.moname.commons.domain.entity.Identifiable;
//
//final class StreamableRepository<T extends Identifiable<ID>, ID extends Serializable> implements IdentifiableRepository<T, ID> {
//
//	@Nonnull
//	private final Supplier<Stream<T>> supplier;
//
//	private StreamableRepository(@Nonnull final Supplier<Stream<T>> supplier) {
//		this.supplier = supplier;
//	}
//
//	@Nonnull
//	static <T extends Identifiable<ID>, ID extends Serializable> StreamableRepository<T, ID> of(@Nonnull final Supplier<Stream<T>> supplier) {
//		return new StreamableRepository<>(supplier);
//	}
//
//	@Nonnull
//	@Override
//	public Iterator<T> iterator() {
//		return stream().iterator();
//	}
//
//	@Nonnull
//	@Override
//	public Stream<T> stream() {
//		return supplier.get();
//	}
//}
