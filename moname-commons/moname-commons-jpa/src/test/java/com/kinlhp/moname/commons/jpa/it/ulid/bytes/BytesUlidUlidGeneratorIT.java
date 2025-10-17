package com.kinlhp.moname.commons.jpa.it.ulid.bytes;

import jakarta.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesUlidUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl.SimpleBytesUlidUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.bytes.BytesUlidUlidIdentifiableRepository;

import static com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style.ULID;

/**
 * Tests for Byte array {@link Style#ULID ULID} styled ULID identifiable entities.
 */
class BytesUlidUlidGeneratorIT extends AbstractBytesUlidGeneratorIT<AbstractBytesUlidUlidIdentifiable> {

	@Autowired
	@Nonnull
	private BytesUlidUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractBytesUlidUlidIdentifiable createEntity() {
		return SimpleBytesUlidUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return ULID;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractBytesUlidUlidIdentifiable, byte[]> getRepository() {
		return repository;
	}
}
