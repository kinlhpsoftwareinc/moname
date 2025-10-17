package com.kinlhp.moname.commons.jpa.it.ulid.ulid;

import jakarta.annotation.Nonnull;

import com.github.f4b6a3.ulid.Ulid;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidDefaultUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl.SimpleUlidDefaultUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.ulid.UlidDefaultUlidIdentifiableRepository;

/**
 * Tests for ULID default styled ULID identifiable entities.
 */
class UlidDefaultUlidGeneratorIT extends AbstractUlidUlidGeneratorIT<AbstractUlidDefaultUlidIdentifiable> {

	@Autowired
	private UlidDefaultUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractUlidDefaultUlidIdentifiable createEntity() {
		return SimpleUlidDefaultUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return Style.AUTO;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractUlidDefaultUlidIdentifiable, Ulid> getRepository() {
		return repository;
	}
}
