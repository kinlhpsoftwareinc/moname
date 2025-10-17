package com.kinlhp.moname.commons.jpa.repository.ulid.string;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringAutoUlidIdentifiable;

/**
 * String {@link com.kinlhp.shaded.org.hibernate.annotations.UlidGenerator.Style#AUTO AUTO} ULID identifiable test
 * repository.
 */
@Repository
public interface StringAutoUlidIdentifiableRepository
		extends StringUlidIdentifiableRepository<AbstractStringAutoUlidIdentifiable> {
}
