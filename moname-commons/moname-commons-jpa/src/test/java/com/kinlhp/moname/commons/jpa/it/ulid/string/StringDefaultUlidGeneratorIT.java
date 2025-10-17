package com.kinlhp.moname.commons.jpa.it.ulid.string;

import jakarta.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringDefaultUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.string.impl.SimpleStringDefaultUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.string.StringDefaultUlidIdentifiableRepository;

/**
 * Tests for String default styled ULID identifiable entities.
 */
class StringDefaultUlidGeneratorIT extends AbstractStringUlidGeneratorIT<AbstractStringDefaultUlidIdentifiable> {

	@Autowired
	@Nonnull
	private StringDefaultUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractStringDefaultUlidIdentifiable createEntity() {
		return SimpleStringDefaultUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return Style.AUTO;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractStringDefaultUlidIdentifiable, String> getRepository() {
		return repository;
	}
}
