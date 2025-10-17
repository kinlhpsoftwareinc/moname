package com.kinlhp.moname.commons.jpa.entity.ulid.string.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringHashUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringHashUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.string.impl.SimpleStringHashUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleStringHashUlidIdentifiable extends AbstractStringHashUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -1879328394418083504L;

	@Nonnull
	public static final String ENTITY = "SimpleStringHashUlidIdentifiable";

	public SimpleStringHashUlidIdentifiable(@Nonnull final String pk) {
		super(pk);
	}
}
