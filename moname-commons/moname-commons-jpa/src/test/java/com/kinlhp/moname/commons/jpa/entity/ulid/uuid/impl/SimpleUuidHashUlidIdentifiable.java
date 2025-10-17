package com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl;

import java.io.Serial;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidHashUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidHashUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl.SimpleUuidHashUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleUuidHashUlidIdentifiable extends AbstractUuidHashUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -1910214326619962607L;

	@Nonnull
	public static final String ENTITY = "SimpleUuidHashUlidIdentifiable";

	public SimpleUuidHashUlidIdentifiable(@Nonnull final UUID pk) {
		super(pk);
	}
}
