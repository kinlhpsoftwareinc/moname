package com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import com.github.f4b6a3.ulid.Ulid;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidUlidUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidUlidUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl.SimpleUlidUlidUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleUlidUlidUlidIdentifiable extends AbstractUlidUlidUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = 6451093345092468097L;

	@Nonnull
	public static final String ENTITY = "SimpleUlidUlidUlidIdentifiable";

	public SimpleUlidUlidUlidIdentifiable(@Nonnull final Ulid pk) {
		super(pk);
	}
}
