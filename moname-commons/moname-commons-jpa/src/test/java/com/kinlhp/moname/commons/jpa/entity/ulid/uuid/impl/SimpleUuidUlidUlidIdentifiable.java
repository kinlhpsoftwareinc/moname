package com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl;

import java.io.Serial;
import java.util.UUID;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidUlidUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidUlidUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl.SimpleUuidUlidUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleUuidUlidUlidIdentifiable extends AbstractUuidUlidUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -2344128559189640164L;

	public static final String ENTITY = "SimpleUuidUlidUlidIdentifiable";

	public SimpleUuidUlidUlidIdentifiable(final UUID pk) {
		super(pk);
	}
}
