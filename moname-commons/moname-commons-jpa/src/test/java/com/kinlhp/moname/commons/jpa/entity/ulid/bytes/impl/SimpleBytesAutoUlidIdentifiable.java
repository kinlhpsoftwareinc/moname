package com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesAutoUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesAutoUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl.SimpleBytesAutoUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleBytesAutoUlidIdentifiable extends AbstractBytesAutoUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = 4377040707600745805L;

	@Nonnull
	public static final String ENTITY = "SimpleBytesAutoUlidIdentifiable";

	public SimpleBytesAutoUlidIdentifiable(@Nonnull final byte[] pk) {
		super(pk);
	}
}
