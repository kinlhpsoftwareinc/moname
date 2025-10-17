package com.kinlhp.moname.commons.jpa.it.ulid.ulid;

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

	@Override
	public AbstractUlidDefaultUlidIdentifiable createEntity() {
		return SimpleUlidDefaultUlidIdentifiable.builder().build();
	}

	@Override
	public Style getExpectedStyle() {
		return Style.AUTO;
	}

	@Override
	public UlidIdentifiableRepository<AbstractUlidDefaultUlidIdentifiable, Ulid> getRepository() {
		return repository;
	}
}
