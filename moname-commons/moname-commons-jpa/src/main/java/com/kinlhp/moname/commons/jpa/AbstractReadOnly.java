package com.kinlhp.moname.commons.jpa;

import java.io.Serial;
import java.io.Serializable;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Abstract implementation for read-only entities.
 */
@EntityListeners(value = {ReadOnlyListener.class})
@EqualsAndHashCode
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class AbstractReadOnly<PK extends Serializable> implements Readable<PK> {

	@Serial
	private static final long serialVersionUID = -5294673564934765394L;

	@Getter
	@Id
	@Setter(onParam_ = {@NotNull})
	private PK pk;

}
