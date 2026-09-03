package com.kinlhp.moname.commons.jpa.repository.ulid.ulid;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidAutoUlidIdentifiable;

/**
 * ULID {@link com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style#AUTO AUTO} ULID identifiable test
 * repository.
 */
@Repository
public interface UlidAutoUlidIdentifiableRepository
		extends UlidUlidIdentifiableRepository<AbstractUlidAutoUlidIdentifiable> {
}
