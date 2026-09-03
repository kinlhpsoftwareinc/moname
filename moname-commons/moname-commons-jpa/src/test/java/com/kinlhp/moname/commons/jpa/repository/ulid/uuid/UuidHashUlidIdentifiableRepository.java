package com.kinlhp.moname.commons.jpa.repository.ulid.uuid;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidHashUlidIdentifiable;

/**
 * UUID {@link com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style#HASH HASH} ULID identifiable test
 * repository.
 */
@Repository
public interface UuidHashUlidIdentifiableRepository
		extends UuidUlidIdentifiableRepository<AbstractUuidHashUlidIdentifiable> {
}
