package com.kinlhp.moname.commons.jpa.entity;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

/**
 * Contract for readable entities.
 */
@SuppressWarnings("java:S119")
public interface Readable<PK extends Serializable> extends Serializable {

	@Nonnull
	PK getPk();
}
