package com.kinlhp.moname.commons.jpa;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

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
	@Setter(onParam = @__({@NotNull}))
	private PK pk;

}
