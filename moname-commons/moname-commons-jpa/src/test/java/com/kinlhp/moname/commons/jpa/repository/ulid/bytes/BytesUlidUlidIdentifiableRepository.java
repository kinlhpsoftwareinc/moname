package com.kinlhp.moname.commons.jpa.repository.ulid.bytes;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesUlidUlidIdentifiable;

/**
 * Byte array {@link com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style#ULID ULID} ULID identifiable test
 * repository.
 */
@Repository
public interface BytesUlidUlidIdentifiableRepository
		extends BytesUlidIdentifiableRepository<AbstractBytesUlidUlidIdentifiable> {
}
