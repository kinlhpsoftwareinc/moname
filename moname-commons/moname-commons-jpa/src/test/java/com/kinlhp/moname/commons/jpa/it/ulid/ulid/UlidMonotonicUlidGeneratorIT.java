package com.kinlhp.moname.commons.jpa.it.ulid.ulid;

import com.github.f4b6a3.ulid.Ulid;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidMonotonicUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl.SimpleUlidMonotonicUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.ulid.UlidMonotonicUlidIdentifiableRepository;

/**
 * Tests for ULID {@link Style#MONOTONIC MONOTONIC} styled ULID identifiable entities.
 */
class UlidMonotonicUlidGeneratorIT extends AbstractUlidUlidGeneratorIT<AbstractUlidMonotonicUlidIdentifiable> {

	@Autowired
	private UlidMonotonicUlidIdentifiableRepository repository;

	@Override
	public AbstractUlidMonotonicUlidIdentifiable createEntity() {
		return SimpleUlidMonotonicUlidIdentifiable.builder().build();
	}

	@Override
	public Style getExpectedStyle() {
		return Style.MONOTONIC;
	}

	@Override
	public UlidIdentifiableRepository<AbstractUlidMonotonicUlidIdentifiable, Ulid> getRepository() {
		return repository;
	}
}
