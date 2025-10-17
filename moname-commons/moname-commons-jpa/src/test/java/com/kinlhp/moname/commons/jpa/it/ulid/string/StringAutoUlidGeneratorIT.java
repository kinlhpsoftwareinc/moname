package com.kinlhp.moname.commons.jpa.it.ulid.string;

import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringAutoUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.string.impl.SimpleStringAutoUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.string.StringAutoUlidIdentifiableRepository;

/**
 * Tests for String {@link Style#AUTO AUTO} styled ULID identifiable entities.
 */
class StringAutoUlidGeneratorIT extends AbstractStringUlidGeneratorIT<AbstractStringAutoUlidIdentifiable> {

	@Autowired
	private StringAutoUlidIdentifiableRepository repository;

	@Override
	public AbstractStringAutoUlidIdentifiable createEntity() {
		return SimpleStringAutoUlidIdentifiable.builder().build();
	}

	@Override
	public Style getExpectedStyle() {
		return Style.AUTO;
	}

	@Override
	public UlidIdentifiableRepository<AbstractStringAutoUlidIdentifiable, String> getRepository() {
		return repository;
	}
}
