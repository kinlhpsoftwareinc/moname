package com.kinlhp.moname.commons.jpa;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Abstract implementation for read-only entities.
 */
@EntityListeners(value = {ReadOnlyListener.class})
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class AbstractReadOnly<PK extends Serializable> implements Readable<PK> {

	@Serial
	private static final long serialVersionUID = 5359284114984076370L;

	@Getter
	@Id
	@Setter(onParam = @__({@NotNull}))
	private PK pk;

}
