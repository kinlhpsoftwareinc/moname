package com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesMonotonicUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesMonotonicUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl.SimpleBytesMonotonicUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleBytesMonotonicUlidIdentifiable extends AbstractBytesMonotonicUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -3505814862334476695L;

	@Nonnull
	public static final String ENTITY = "SimpleBytesMonotonicUlidIdentifiable";

	public SimpleBytesMonotonicUlidIdentifiable(@Nonnull final byte[] pk) {
		super(pk);
	}
}
