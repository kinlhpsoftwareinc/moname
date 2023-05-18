package com.kinlhp.moname.commons.jpa;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

/**
 * Contract for persistable entities.
 */
public interface Persistable<PK extends Serializable> extends Readable<PK> {

	/**
	 * Sets the value of the primary key (PK).
	 *
	 * @param pk primary key (PK) value.
	 */
	void setPK(@Nonnull final PK pk);
}
