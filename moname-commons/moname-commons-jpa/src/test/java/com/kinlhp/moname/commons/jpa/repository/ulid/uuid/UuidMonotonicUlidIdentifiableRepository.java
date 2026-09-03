package com.kinlhp.moname.commons.jpa.repository.ulid.uuid;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidMonotonicUlidIdentifiable;

/**
 * UUID {@link com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style#MONOTONIC MONOTONIC} ULID identifiable
 * test repository.
 */
@Repository
public interface UuidMonotonicUlidIdentifiableRepository
		extends UuidUlidIdentifiableRepository<AbstractUuidMonotonicUlidIdentifiable> {
}
