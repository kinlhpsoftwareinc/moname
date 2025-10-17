package com.kinlhp.moname.commons.jpa.it.ulid.string;

import jakarta.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringMonotonicUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.string.impl.SimpleStringMonotonicUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.string.StringMonotonicUlidIdentifiableRepository;

/**
 * Tests for String {@link Style#MONOTONIC MONOTONIC} styled ULID identifiable entities.
 */
class StringMonotonicUlidGeneratorIT extends AbstractStringUlidGeneratorIT<AbstractStringMonotonicUlidIdentifiable> {

	@Autowired
	@Nonnull
	private StringMonotonicUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractStringMonotonicUlidIdentifiable createEntity() {
		return SimpleStringMonotonicUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return Style.MONOTONIC;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractStringMonotonicUlidIdentifiable, String> getRepository() {
		return repository;
	}
}
