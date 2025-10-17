package com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import com.github.f4b6a3.ulid.Ulid;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidMonotonicUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidMonotonicUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl.SimpleUlidMonotonicUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleUlidMonotonicUlidIdentifiable extends AbstractUlidMonotonicUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -4380375232520863961L;

	@Nonnull
	public static final String ENTITY = "SimpleUlidMonotonicUlidIdentifiable";

	public SimpleUlidMonotonicUlidIdentifiable(@Nonnull final Ulid pk) {
		super(pk);
	}
}
