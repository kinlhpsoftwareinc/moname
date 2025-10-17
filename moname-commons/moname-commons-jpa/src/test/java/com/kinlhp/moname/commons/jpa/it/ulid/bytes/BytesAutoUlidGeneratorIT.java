package com.kinlhp.moname.commons.jpa.it.ulid.bytes;

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
	private BytesAutoUlidIdentifiableRepository repository;

	@Override
	public AbstractBytesAutoUlidIdentifiable createEntity() {
		return SimpleBytesAutoUlidIdentifiable.builder().build();
	}

	@Override
	public Style getExpectedStyle() {
		return AUTO;
	}

	@Override
	public UlidIdentifiableRepository<AbstractBytesAutoUlidIdentifiable, byte[]> getRepository() {
		return repository;
	}
}
