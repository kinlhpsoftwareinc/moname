package com.kinlhp.moname.commons.jpa.repository.ulid.string;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringUlidUlidIdentifiable;

/**
 * String {@link com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style#ULID ULID} ULID identifiable test
 * repository.
 */
@Repository
public interface StringUlidUlidIdentifiableRepository
		extends StringUlidIdentifiableRepository<AbstractStringUlidUlidIdentifiable> {
}
