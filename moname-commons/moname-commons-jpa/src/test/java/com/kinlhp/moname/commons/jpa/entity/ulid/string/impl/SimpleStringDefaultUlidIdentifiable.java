package com.kinlhp.moname.commons.jpa.entity.ulid.string.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringDefaultUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringDefaultUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.string.impl.SimpleStringDefaultUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleStringDefaultUlidIdentifiable extends AbstractStringDefaultUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -5054335332536746980L;

	@Nonnull
	public static final String ENTITY = "SimpleStringDefaultUlidIdentifiable";

	public SimpleStringDefaultUlidIdentifiable(@Nonnull final String pk) {
		super(pk);
	}
}
