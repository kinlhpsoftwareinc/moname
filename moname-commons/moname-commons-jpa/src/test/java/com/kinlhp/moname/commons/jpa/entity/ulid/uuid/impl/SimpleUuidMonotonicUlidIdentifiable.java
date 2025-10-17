package com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl;

import java.io.Serial;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidMonotonicUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidMonotonicUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl.SimpleUuidMonotonicUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleUuidMonotonicUlidIdentifiable extends AbstractUuidMonotonicUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -4161113324284482767L;

	@Nonnull
	public static final String ENTITY = "SimpleUuidMonotonicUlidIdentifiable";

	public SimpleUuidMonotonicUlidIdentifiable(@Nonnull final UUID pk) {
		super(pk);
	}
}
