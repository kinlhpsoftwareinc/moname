package com.kinlhp.moname.commons.jpa.repository.ulid.uuid;

import java.util.UUID;

import org.springframework.data.repository.NoRepositoryBean;

import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;

/**
 * UUID ULID identifiable test repository.
 */
@NoRepositoryBean
public interface UuidUlidIdentifiableRepository<T extends AbstractUuidUlidIdentifiable>
		extends UlidIdentifiableRepository<T, UUID> {
}
