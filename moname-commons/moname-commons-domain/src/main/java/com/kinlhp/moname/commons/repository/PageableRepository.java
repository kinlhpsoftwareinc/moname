//package com.kinlhp.moname.commons.repository;
//
//import java.io.Serializable;
//import java.util.Optional;
//
//import jakarta.annotation.Nonnull;
//
//import org.springframework.data.domain.Page;
//
//import com.kinlhp.moname.commons.domain.entity.Identifiable;
//import com.kinlhp.moname.commons.util.SingletonCollector;
//
//public interface PageableRepository<T extends Identifiable<ID>, ID extends Serializable> extends IdentifiableRepository<T, ID>, Page<T> {
//
//	/**
//	 * By convention, should be compliant with {@link SingletonCollector#toSingleton()}.
//	 */
//	@Nonnull
//	@Override
//	default Optional<T> one(@Nonnull final ID identity) {
//		return stream().filter(identifiable -> identifiable.is(identity)).collect(SingletonCollector.toSingleton());
//	}
//}
