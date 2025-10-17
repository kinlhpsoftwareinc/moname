package com.kinlhp.moname.commons.jpa.entity.ulid.ulid;

import java.io.Serial;

import jakarta.annotation.Nonnull;

import com.github.f4b6a3.ulid.Ulid;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable;

@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
public abstract class AbstractUlidUlidIdentifiable extends AbstractUlidIdentifiable<Ulid> {

	@Serial
	private static final long serialVersionUID = 220419961251935405L;

	@Nonnull
	protected static final String TABLE = "ulid_ulid_identifiable";

	protected AbstractUlidUlidIdentifiable(@Nonnull final Ulid pk) {
		super(pk);
	}
}
