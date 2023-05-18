package com.kinlhp.moname.commons.jpa;

import java.io.Serial;
import java.io.Serializable;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

/**
 * Abstract implementation for read-only entities.
 */
@EntityListeners(value = {ReadOnlyListener.class})
//@EqualsAndHashCode
@MappedSuperclass
//@NoArgsConstructor(onConstructor_ = {@Nonnull})
//@SuperBuilder(toBuilder = true)
public abstract class AbstractReadOnly<PK extends Serializable> implements Readable<PK> {

	@Serial
	private static final long serialVersionUID = 1840618276170713780L;

	//	@Getter(onMethod_ = {@Nonnull})
	@Id
	@NotNull
//	@Setter(onParam_ = {@Nonnull})
	private PK pk;

	@Nonnull
	@Override
	public PK getPK() {
		return pk;
	}

	public void setPK(@Nonnull final PK pk) {
		this.pk = pk;
	}
}
