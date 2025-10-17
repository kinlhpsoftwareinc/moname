package com.kinlhp.moname.commons.jpa.it.ulid.string;

import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringUlidUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.string.impl.SimpleStringUlidUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.string.StringUlidUlidIdentifiableRepository;

/**
 * Tests for String {@link Style#ULID ULID} styled ULID identifiable entities.
 */
class StringUlidUlidGeneratorIT extends AbstractStringUlidGeneratorIT<AbstractStringUlidUlidIdentifiable> {

	@Autowired
	private StringUlidUlidIdentifiableRepository repository;

	@Override
	public AbstractStringUlidUlidIdentifiable createEntity() {
		return SimpleStringUlidUlidIdentifiable.builder().build();
	}

	@Override
	public Style getExpectedStyle() {
		return Style.ULID;
	}

	@Override
	public UlidIdentifiableRepository<AbstractStringUlidUlidIdentifiable, String> getRepository() {
		return repository;
	}
}
