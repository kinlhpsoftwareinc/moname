package com.kinlhp.moname.commons.jpa.repository.ulid.bytes;

import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesDefaultUlidIdentifiable;

/**
 * Byte array default ULID identifiable test repository.
 */
@Repository
public interface BytesDefaultUlidIdentifiableRepository
		extends BytesUlidIdentifiableRepository<AbstractBytesDefaultUlidIdentifiable> {
}
