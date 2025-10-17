package com.kinlhp.moname.commons.jpa.repository.ulid.bytes;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesHashUlidIdentifiable;

/**
 * Byte array {@link com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style#HASH HASH} ULID identifiable test
 * repository.
 */
@Repository
public interface BytesHashUlidIdentifiableRepository
		extends BytesUlidIdentifiableRepository<AbstractBytesHashUlidIdentifiable> {
}
