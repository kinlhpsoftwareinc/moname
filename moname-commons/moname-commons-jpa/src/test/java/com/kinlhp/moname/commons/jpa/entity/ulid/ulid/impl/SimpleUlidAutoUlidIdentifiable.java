package com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import com.github.f4b6a3.ulid.Ulid;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidAutoUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidAutoUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl.SimpleUlidAutoUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleUlidAutoUlidIdentifiable extends AbstractUlidAutoUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -3489718182504939728L;

	@Nonnull
	public static final String ENTITY = "SimpleUlidAutoUlidIdentifiable";

	public SimpleUlidAutoUlidIdentifiable(@Nonnull final Ulid pk) {
		super(pk);
	}
}
