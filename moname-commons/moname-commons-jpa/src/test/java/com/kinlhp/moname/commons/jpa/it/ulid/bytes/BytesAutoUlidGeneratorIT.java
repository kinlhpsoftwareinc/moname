package com.kinlhp.moname.commons.jpa.it.ulid.bytes;

import jakarta.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesAutoUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl.SimpleBytesAutoUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.bytes.BytesAutoUlidIdentifiableRepository;

import static com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style.AUTO;

/**
 * Tests for Byte array {@link Style#AUTO AUTO} styled ULID identifiable entities.
 */
class BytesAutoUlidGeneratorIT extends AbstractBytesUlidGeneratorIT<AbstractBytesAutoUlidIdentifiable> {

	@Autowired
	@Nonnull
	private BytesAutoUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractBytesAutoUlidIdentifiable createEntity() {
		return SimpleBytesAutoUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return AUTO;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractBytesAutoUlidIdentifiable, byte[]> getRepository() {
		return repository;
	}
}
