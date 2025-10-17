package com.kinlhp.moname.commons.jpa.entity.ulid.string;

import java.io.Serial;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable;

@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
public abstract class AbstractStringUlidIdentifiable extends AbstractUlidIdentifiable<String> {

	@Serial
	private static final long serialVersionUID = 8891903322746745025L;

	@Nonnull
	protected static final String TABLE = "string_ulid_identifiable";

	protected AbstractStringUlidIdentifiable(@Nonnull final String pk) {
		super(pk);
	}
}
