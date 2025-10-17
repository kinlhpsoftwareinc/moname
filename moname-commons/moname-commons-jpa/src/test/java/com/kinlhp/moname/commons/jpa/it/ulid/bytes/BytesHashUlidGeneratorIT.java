package com.kinlhp.moname.commons.jpa.it.ulid.bytes;

import jakarta.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesHashUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl.SimpleBytesHashUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.bytes.BytesHashUlidIdentifiableRepository;

import static com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style.HASH;

/**
 * Tests for Byte array {@link Style#HASH HASH} styled ULID identifiable entities.
 */
class BytesHashUlidGeneratorIT extends AbstractBytesUlidGeneratorIT<AbstractBytesHashUlidIdentifiable> {

	@Autowired
	@Nonnull
	private BytesHashUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractBytesHashUlidIdentifiable createEntity() {
		return SimpleBytesHashUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return HASH;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractBytesHashUlidIdentifiable, byte[]> getRepository() {
		return repository;
	}
}
