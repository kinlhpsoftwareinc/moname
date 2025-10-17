package com.kinlhp.moname.commons.jpa.entity;

import java.io.Serializable;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.EventType;

/**
 * Contract for persistable entities.
 */
@SuppressWarnings("java:S119")
public interface Persistable<PK extends Serializable> extends Serializable {

	/**
	 * The current value assigned to the property, or {@code null}.
	 *
	 * @see org.hibernate.generator.BeforeExecutionGenerator#generate(SharedSessionContractImplementor, Object, Object, EventType)
	 * generate
	 */
	@Nullable
	PK getPk();

	void setPk(@Nonnull final PK pk);
}
