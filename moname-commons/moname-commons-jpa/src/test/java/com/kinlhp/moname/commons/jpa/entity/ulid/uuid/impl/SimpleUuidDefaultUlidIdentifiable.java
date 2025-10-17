package com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl;

import java.io.Serial;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidDefaultUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidDefaultUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl.SimpleUuidDefaultUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleUuidDefaultUlidIdentifiable extends AbstractUuidDefaultUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = 1226943040322136336L;

	@Nonnull
	public static final String ENTITY = "SimpleUuidDefaultUlidIdentifiable";

	public SimpleUuidDefaultUlidIdentifiable(@Nonnull final UUID pk) {
		super(pk);
	}
}
