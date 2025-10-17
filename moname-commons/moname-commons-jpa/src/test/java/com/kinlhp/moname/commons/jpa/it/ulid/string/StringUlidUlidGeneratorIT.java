package com.kinlhp.moname.commons.jpa.it.ulid.string;

import jakarta.annotation.Nonnull;

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
	@Nonnull
	private StringUlidUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractStringUlidUlidIdentifiable createEntity() {
		return SimpleStringUlidUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return Style.ULID;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractStringUlidUlidIdentifiable, String> getRepository() {
		return repository;
	}
}
