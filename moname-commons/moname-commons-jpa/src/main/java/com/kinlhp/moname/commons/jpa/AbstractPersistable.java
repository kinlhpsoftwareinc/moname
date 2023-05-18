package com.kinlhp.moname.commons.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

/**
 * Abstract implementation for persistable entities.
 */
//@Data
@MappedSuperclass
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

	@Column(updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Getter(onMethod_ = {@Nonnull})
	@Id
	@NotNull
//	@Setter(onParam_ = {@Nonnull})
	private PK pk;
}
