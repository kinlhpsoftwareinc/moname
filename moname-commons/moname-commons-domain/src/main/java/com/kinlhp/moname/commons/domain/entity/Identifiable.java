package com.kinlhp.moname.commons.domain.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Identifiable entity contract.
 *
 * @param <ID> the entity's identity (ID) type.
 */
@SuppressWarnings("java:S119")
public interface Identifiable<ID extends Serializable> extends Serializable {

	ID getIdentity();

	default boolean is(final ID identity) {
		return Objects.equals(getIdentity(), identity);
	}
}
