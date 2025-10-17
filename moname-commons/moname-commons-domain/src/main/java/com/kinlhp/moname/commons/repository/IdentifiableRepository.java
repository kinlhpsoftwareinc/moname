package com.kinlhp.moname.commons.repository;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Supplier;

import jakarta.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kinlhp.moname.commons.domain.entity.Identifiable;
import com.kinlhp.moname.commons.stream.SingletonCollector;

/**
 * @param <T>  the entity's type.
 * @param <ID> the entity's identity (ID) type.
 */
@SuppressWarnings("java:S119")
public interface IdentifiableRepository<T extends Identifiable<ID>, ID extends Serializable> {

	// TODO: https://softwareengineering.stackexchange.com/a/396159

	@Nonnull
	Logger LOG = LoggerFactory.getLogger(IdentifiableRepository.class);

	/**
	 * By convention, should be compliant with {@link SingletonCollector#toSingle() toSingle}.
	 *
	 * @param identity identity (ID) by which the entity is identified.
	 * @return single entity.
	 */
	@Nonnull
	Optional<T> one(@Nonnull final ID identity);

	/**
	 * @see IdentifiableRepository#one(Serializable) one
	 */
	@Nonnull
	default Optional<T> one(@Nonnull final Supplier<ID> identitySupplier) {
		LOG.debug("Searching for a single according to the identity supplier");
		return one(identitySupplier.get());
	}
}
