package com.kinlhp.moname.commons.jpa.repository.ulid.string;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringDefaultUlidIdentifiable;

/**
 * String default ULID identifiable test repository.
 */
@Repository
public interface StringDefaultUlidIdentifiableRepository
		extends StringUlidIdentifiableRepository<AbstractStringDefaultUlidIdentifiable> {
}
