package com.kinlhp.moname.commons.jpa.it.ulid.string;

import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringHashUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.string.impl.SimpleStringHashUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.string.StringHashUlidIdentifiableRepository;

/**
 * Tests for String {@link Style#HASH HASH} styled ULID identifiable entities.
 */
class StringHashUlidGeneratorIT extends AbstractStringUlidGeneratorIT<AbstractStringHashUlidIdentifiable> {

	@Autowired
	private StringHashUlidIdentifiableRepository repository;

	@Override
	public AbstractStringHashUlidIdentifiable createEntity() {
		return SimpleStringHashUlidIdentifiable.builder().build();
	}

	@Override
	public Style getExpectedStyle() {
		return Style.HASH;
	}

	@Override
	public UlidIdentifiableRepository<AbstractStringHashUlidIdentifiable, String> getRepository() {
		return repository;
	}
}
