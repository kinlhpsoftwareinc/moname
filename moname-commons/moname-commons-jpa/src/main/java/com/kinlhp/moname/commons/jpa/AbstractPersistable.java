package com.kinlhp.moname.commons.jpa;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

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
	@Getter(onMethod = @__({ @Nullable }))
	@Id
	@Setter(onParam = @__({ @Nullable }))
	private PK pk;

}
