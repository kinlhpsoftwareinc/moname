package com.kinlhp.moname.commons.jpa.entity.ulid.string.impl;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringUlidUlidIdentifiable;

import static com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringUlidUlidIdentifiable.DISCRIMINATOR_VALUE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.string.impl.SimpleStringUlidUlidIdentifiable.ENTITY;

@DiscriminatorValue(DISCRIMINATOR_VALUE)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SimpleStringUlidUlidIdentifiable extends AbstractStringUlidUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = 2739906778984717986L;

	@Nonnull
	public static final String ENTITY = "SimpleStringUlidUlidIdentifiable";

	public SimpleStringUlidUlidIdentifiable(@Nonnull final String pk) {
		super(pk);
	}
}
