package com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesUlidUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesUlidUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl.SimpleBytesUlidUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleBytesUlidUlidIdentifiable extends AbstractBytesUlidUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = 5426324495865495246L;

	@Nonnull
	public static final String ENTITY = "SimpleBytesUlidUlidIdentifiable";

	public SimpleBytesUlidUlidIdentifiable(@Nonnull final byte[] pk) {
		super(pk);
	}
}
