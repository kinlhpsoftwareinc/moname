package com.kinlhp.moname.commons.jpa.it.ulid.ulid;

import com.github.f4b6a3.ulid.Ulid;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidAutoUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.impl.SimpleUlidAutoUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.ulid.UlidAutoUlidIdentifiableRepository;

/**
 * Tests for ULID {@link Style#AUTO AUTO} styled ULID identifiable entities.
 */
class UlidAutoUlidGeneratorIT extends AbstractUlidUlidGeneratorIT<AbstractUlidAutoUlidIdentifiable> {

	@Autowired
	private UlidAutoUlidIdentifiableRepository repository;

	@Override
	public AbstractUlidAutoUlidIdentifiable createEntity() {
		return SimpleUlidAutoUlidIdentifiable.builder().build();
	}

	@Override
	public Style getExpectedStyle() {
		return Style.AUTO;
	}

	@Override
	public UlidIdentifiableRepository<AbstractUlidAutoUlidIdentifiable, Ulid> getRepository() {
		return repository;
	}
}
