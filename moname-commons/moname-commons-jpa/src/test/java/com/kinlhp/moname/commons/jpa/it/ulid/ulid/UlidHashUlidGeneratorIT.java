package com.kinlhp.moname.commons.jpa.it.ulid.ulid;

import jakarta.annotation.Nonnull;

import com.github.f4b6a3.ulid.Ulid;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidHashUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl.SimpleUlidHashUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.ulid.UlidHashUlidIdentifiableRepository;

/**
 * Tests for ULID {@link Style#HASH HASH} styled ULID identifiable entities.
 */
class UlidHashUlidGeneratorIT extends AbstractUlidUlidGeneratorIT<AbstractUlidHashUlidIdentifiable> {

	@Autowired
	private UlidHashUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractUlidHashUlidIdentifiable createEntity() {
		return SimpleUlidHashUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return Style.HASH;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractUlidHashUlidIdentifiable, Ulid> getRepository() {
		return repository;
	}
}
