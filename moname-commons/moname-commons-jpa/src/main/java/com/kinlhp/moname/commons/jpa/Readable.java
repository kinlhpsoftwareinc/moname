package com.kinlhp.moname.commons.jpa;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

/**
 * Contract for readable entities.
 */
public interface Readable<PK extends Serializable> extends Serializable {

	/**
	 * Gets the value of the primary key (PK).
	 *
	 * @return primary key (PK) value.
	 */
	@Nonnull
	PK getPK();
}
