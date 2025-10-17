package com.kinlhp.moname.commons.jpa.it.ulid.string;

import jakarta.annotation.Nonnull;

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
	@Nonnull
	private StringAutoUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractStringAutoUlidIdentifiable createEntity() {
		return SimpleStringAutoUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return Style.AUTO;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractStringAutoUlidIdentifiable, String> getRepository() {
		return repository;
	}
}
