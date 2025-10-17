package com.kinlhp.moname.commons.jpa.repository.ulid.bytes;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesAutoUlidIdentifiable;

/**
 * Byte array {@link com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style#AUTO AUTO} ULID identifiable test
 * repository.
 */
@Repository
public interface BytesAutoUlidIdentifiableRepository
		extends BytesUlidIdentifiableRepository<AbstractBytesAutoUlidIdentifiable> {
}
