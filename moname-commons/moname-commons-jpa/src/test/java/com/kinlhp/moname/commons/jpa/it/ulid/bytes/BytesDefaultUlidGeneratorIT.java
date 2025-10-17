package com.kinlhp.moname.commons.jpa.it.ulid.bytes;

import jakarta.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesDefaultUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl.SimpleBytesDefaultUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.bytes.BytesDefaultUlidIdentifiableRepository;

import static com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style.AUTO;

/**
 * Tests for Byte array default styled ULID identifiable entities.
 */
class BytesDefaultUlidGeneratorIT extends AbstractBytesUlidGeneratorIT<AbstractBytesDefaultUlidIdentifiable> {

	@Autowired
	@Nonnull
	private BytesDefaultUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractBytesDefaultUlidIdentifiable createEntity() {
		return SimpleBytesDefaultUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return AUTO;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractBytesDefaultUlidIdentifiable, byte[]> getRepository() {
		return repository;
	}
}
