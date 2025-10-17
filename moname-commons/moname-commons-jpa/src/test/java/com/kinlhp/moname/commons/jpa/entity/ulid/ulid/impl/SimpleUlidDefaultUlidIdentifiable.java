package com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import com.github.f4b6a3.ulid.Ulid;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidDefaultUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidDefaultUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl.SimpleUlidDefaultUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleUlidDefaultUlidIdentifiable extends AbstractUlidDefaultUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -2064916697716046360L;

	@Nonnull
	public static final String ENTITY = "SimpleUlidDefaultUlidIdentifiable";

	public SimpleUlidDefaultUlidIdentifiable(@Nonnull final Ulid pk) {
		super(pk);
	}
}
