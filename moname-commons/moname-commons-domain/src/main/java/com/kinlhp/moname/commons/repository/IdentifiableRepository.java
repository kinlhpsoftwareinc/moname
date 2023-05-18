package com.kinlhp.moname.commons.repository;

import java.io.Serializable;
import java.util.Optional;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.domain.entity.Identifiable;
import com.kinlhp.moname.commons.util.SingletonCollector;

/**
 * @param <T>  the entity's type.
 * @param <ID> the entity's identity (ID) type.
 */
public interface IdentifiableRepository<T extends Identifiable<ID>, ID extends Serializable> {

	/**
	 * By convention, should be compliant with {@link SingletonCollector#toSingleton()}.
	 *
	 * @param identity identity (ID) by which the entity is identified.
	 * @return single entity.
	 */
	@Nonnull
	Optional<T> one(@Nonnull final ID identity);
}
