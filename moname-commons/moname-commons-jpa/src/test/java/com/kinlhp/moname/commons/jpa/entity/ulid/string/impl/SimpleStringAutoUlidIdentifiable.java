package com.kinlhp.moname.commons.jpa.entity.ulid.string.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringAutoUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringAutoUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.string.impl.SimpleStringAutoUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleStringAutoUlidIdentifiable extends AbstractStringAutoUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = 2824397626373809538L;

	@Nonnull
	public static final String ENTITY = "SimpleStringAutoUlidIdentifiable";

	public SimpleStringAutoUlidIdentifiable(@Nonnull final String pk) {
		super(pk);
	}
}
