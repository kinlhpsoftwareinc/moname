package com.kinlhp.moname.commons.jpa.repository.ulid.bytes;

import org.springframework.data.repository.NoRepositoryBean;

import com.kinlhp.moname.commons.jpa.entity.ulid.bytes.AbstractBytesUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;

/**
 * Byte array ULID identifiable test repository.
 */
@NoRepositoryBean
public interface BytesUlidIdentifiableRepository<T extends AbstractBytesUlidIdentifiable>
		extends UlidIdentifiableRepository<T, byte[]> {
}
