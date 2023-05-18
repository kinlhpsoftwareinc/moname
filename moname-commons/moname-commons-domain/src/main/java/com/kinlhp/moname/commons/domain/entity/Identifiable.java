package com.kinlhp.moname.commons.domain.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.annotation.Nonnull;

/**
 * Identifiable entity contract.
 *
 * @param <ID> the entity's identity (ID) type.
 */
public interface Identifiable<ID extends Serializable> extends Serializable {

	/**
	 * Get the identity (ID) of the entity.
	 *
	 * @return identity (ID) of the entity.
	 */
	@Nonnull
	ID getIdentity();

	/**
	 * TODO: Doc
	 *
	 * @param identity
	 * @return
	 */
	default boolean is(@Nonnull final ID identity) {
		return Objects.equals(getIdentity(), identity);
	}
}
