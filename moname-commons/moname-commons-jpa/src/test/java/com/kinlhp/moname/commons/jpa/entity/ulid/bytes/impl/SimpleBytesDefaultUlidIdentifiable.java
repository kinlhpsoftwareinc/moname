package com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesDefaultUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesDefaultUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl.SimpleBytesDefaultUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleBytesDefaultUlidIdentifiable extends AbstractBytesDefaultUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -6558183953503506678L;

	@Nonnull
	public static final String ENTITY = "SimpleBytesDefaultUlidIdentifiable";

	public SimpleBytesDefaultUlidIdentifiable(@Nonnull final byte[] pk) {
		super(pk);
	}
}
