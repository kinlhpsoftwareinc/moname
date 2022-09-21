package com.kinlhp.moname.commons.jpa;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.Setter;

/**
 * Abstract implementation for persistable entities.
 */
@Data
@MappedSuperclass
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

	@Serial
	private static final long serialVersionUID = 4235429260240355001L;

	@Column(updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Setter(onParam = @__({ @NotNull }))
	private PK pk;

}
