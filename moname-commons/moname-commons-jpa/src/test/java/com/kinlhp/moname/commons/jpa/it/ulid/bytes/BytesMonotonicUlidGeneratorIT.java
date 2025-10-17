package com.kinlhp.moname.commons.jpa.it.ulid.bytes;

import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesMonotonicUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.impl.SimpleBytesMonotonicUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.bytes.BytesMonotonicUlidIdentifiableRepository;

import static com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style.MONOTONIC;

/**
 * Tests for Byte array {@link Style#MONOTONIC MONOTONIC} styled ULID identifiable entities.
 */
class BytesMonotonicUlidGeneratorIT extends AbstractBytesUlidGeneratorIT<AbstractBytesMonotonicUlidIdentifiable> {

	@Autowired
	private BytesMonotonicUlidIdentifiableRepository repository;

	@Override
	public AbstractBytesMonotonicUlidIdentifiable createEntity() {
		return SimpleBytesMonotonicUlidIdentifiable.builder().build();
	}

	@Override
	public Style getExpectedStyle() {
		return MONOTONIC;
	}

	@Override
	public UlidIdentifiableRepository<AbstractBytesMonotonicUlidIdentifiable, byte[]> getRepository() {
		return repository;
	}
}
