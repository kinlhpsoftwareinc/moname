package com.kinlhp.moname.commons.jpa.id.ulid;

import java.io.Serializable;

import com.github.f4b6a3.ulid.Ulid;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

/**
 * @see org.hibernate.id.uuid.UuidValueGenerator UuidValueGenerator
 */
public interface UlidValueGenerator extends Serializable {

	/**
	 * Generate the ULID value
	 */
	Ulid generateUlid(final SharedSessionContractImplementor session);
}
