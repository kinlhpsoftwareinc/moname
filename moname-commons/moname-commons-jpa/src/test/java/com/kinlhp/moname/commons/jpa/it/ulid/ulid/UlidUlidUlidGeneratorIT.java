package com.kinlhp.moname.commons.jpa.it.ulid.ulid;

import jakarta.annotation.Nonnull;

import com.github.f4b6a3.ulid.Ulid;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidUlidUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl.SimpleUlidUlidUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.ulid.UlidUlidUlidIdentifiableRepository;

/**
 * Tests for ULID {@link Style#ULID ULID} styled ULID identifiable entities.
 */
class UlidUlidUlidGeneratorIT extends AbstractUlidUlidGeneratorIT<AbstractUlidUlidUlidIdentifiable> {

	@Autowired
	private UlidUlidUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractUlidUlidUlidIdentifiable createEntity() {
		return SimpleUlidUlidUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return Style.ULID;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractUlidUlidUlidIdentifiable, Ulid> getRepository() {
		return repository;
	}
}
