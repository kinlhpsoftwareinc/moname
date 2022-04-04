package com.kinlhp.moname.commons.jpa;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * Abstraction for persistable entities.
 */
public interface Persistable<PK extends Serializable> extends Readable<PK> {

	/**
	 * Defines the value of the primary key (PK).
	 *
	 * @param pk Primary key (PK) value
	 */
	void setPk(@NotNull final PK pk);

}
