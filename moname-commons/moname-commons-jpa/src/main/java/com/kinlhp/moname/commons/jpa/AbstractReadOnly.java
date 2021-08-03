package com.kinlhp.moname.commons.jpa;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * Abstract implementation for read-only entities.
 */
@Data
@EntityListeners(value = { ReadOnlyListener.class })
@MappedSuperclass
public abstract class AbstractReadOnly<PK extends Serializable> implements Readable<PK> {

	@Serial
	private static final long serialVersionUID = -5144077154210515009L;

	@Getter(onMethod = @__({ @Nullable }))
	@Id
	private PK pk;

}
