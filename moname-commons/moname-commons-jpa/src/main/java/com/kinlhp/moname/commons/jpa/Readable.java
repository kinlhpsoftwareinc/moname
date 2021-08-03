package com.kinlhp.moname.commons.jpa;

import java.io.Serializable;

import org.springframework.lang.Nullable;

/**
 * Abstraction for readable entities.
 */
public interface Readable<PK extends Serializable> extends Serializable {

	/**
	 * Obtains the value of the primary key (PK).
	 *
	 * @return Primary key (PK) value
	 */
	@Nullable
	PK getPk();

}
