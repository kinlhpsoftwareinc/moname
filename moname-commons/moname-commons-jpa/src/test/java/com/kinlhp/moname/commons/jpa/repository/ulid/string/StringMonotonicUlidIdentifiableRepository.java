package com.kinlhp.moname.commons.jpa.repository.ulid.string;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringMonotonicUlidIdentifiable;

/**
 * String {@link com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style#MONOTONIC MONOTONIC} ULID identifiable
 * test repository.
 */
@Repository
public interface StringMonotonicUlidIdentifiableRepository
		extends StringUlidIdentifiableRepository<AbstractStringMonotonicUlidIdentifiable> {
}
