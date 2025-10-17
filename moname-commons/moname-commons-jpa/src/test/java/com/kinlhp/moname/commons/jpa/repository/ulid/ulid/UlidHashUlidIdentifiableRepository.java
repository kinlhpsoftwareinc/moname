package com.kinlhp.moname.commons.jpa.repository.ulid.ulid;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidHashUlidIdentifiable;

/**
 * ULID {@link com.kinlhp.shaded.org.hibernate.annotations.UlidGenerator.Style#HASH HASH} ULID identifiable test
 * repository.
 */
@Repository
public interface UlidHashUlidIdentifiableRepository
		extends UlidUlidIdentifiableRepository<AbstractUlidHashUlidIdentifiable> {
}
