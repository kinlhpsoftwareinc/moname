package com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl;

import java.io.Serial;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesHashUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesHashUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl.SimpleBytesHashUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleBytesHashUlidIdentifiable extends AbstractBytesHashUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -257411554741272299L;

	public static final String ENTITY = "SimpleBytesHashUlidIdentifiable";

	public SimpleBytesHashUlidIdentifiable(final byte[] pk) {
		super(pk);
	}
}
