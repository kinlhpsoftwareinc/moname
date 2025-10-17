package com.kinlhp.moname.commons.jpa.repository.ulid.ulid;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidMonotonicUlidIdentifiable;

/**
 * ULID {@link com.kinlhp.shaded.org.hibernate.annotations.UlidGenerator.Style#MONOTONIC MONOTONIC} ULID identifiable
 * test repository.
 */
@Repository
public interface UlidMonotonicUlidIdentifiableRepository
		extends UlidUlidIdentifiableRepository<AbstractUlidMonotonicUlidIdentifiable> {
}
