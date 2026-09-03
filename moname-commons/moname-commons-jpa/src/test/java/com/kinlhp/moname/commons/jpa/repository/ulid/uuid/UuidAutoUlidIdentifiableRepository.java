package com.kinlhp.moname.commons.jpa.repository.ulid.uuid;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidAutoUlidIdentifiable;

/**
 * UUID {@link com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style#AUTO AUTO} ULID identifiable test
 * repository.
 */
@Repository
public interface UuidAutoUlidIdentifiableRepository
		extends UuidUlidIdentifiableRepository<AbstractUuidAutoUlidIdentifiable> {
}
