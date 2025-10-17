package com.kinlhp.moname.commons.jpa.entity.ulid.string.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringMonotonicUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringMonotonicUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.string.impl.SimpleStringMonotonicUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleStringMonotonicUlidIdentifiable extends AbstractStringMonotonicUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = 5292169109078895882L;

	@Nonnull
	public static final String ENTITY = "SimpleStringMonotonicUlidIdentifiable";

	public SimpleStringMonotonicUlidIdentifiable(@Nonnull final String pk) {
		super(pk);
	}
}
