package com.kinlhp.moname.commons.jpa.entity;

import java.io.Serializable;

/**
 * Contract for readable entities.
 */
@SuppressWarnings("java:S119")
public interface Readable<PK extends Serializable> extends Serializable {

	PK getPk();
}
