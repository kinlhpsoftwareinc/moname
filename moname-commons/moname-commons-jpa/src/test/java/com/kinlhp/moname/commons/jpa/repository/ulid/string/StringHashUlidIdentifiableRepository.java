package com.kinlhp.moname.commons.jpa.repository.ulid.string;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringHashUlidIdentifiable;

/**
 * String {@link com.kinlhp.shaded.org.hibernate.annotations.UlidGenerator.Style#HASH HASH} ULID identifiable test
 * repository.
 */
@Repository
public interface StringHashUlidIdentifiableRepository
		extends StringUlidIdentifiableRepository<AbstractStringHashUlidIdentifiable> {
}
