package com.kinlhp.moname.commons.jpa.repository.ulid.bytes;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesMonotonicUlidIdentifiable;

/**
 * Byte array {@link com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style#MONOTONIC MONOTONIC} ULID
 * identifiable test repository.
 */
@Repository
public interface BytesMonotonicUlidIdentifiableRepository
		extends BytesUlidIdentifiableRepository<AbstractBytesMonotonicUlidIdentifiable> {
}
