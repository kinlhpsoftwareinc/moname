package com.kinlhp.moname.commons.jpa.repository.ulid.ulid;

import com.github.f4b6a3.ulid.Ulid;
import org.springframework.data.repository.NoRepositoryBean;

import com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;

/**
 * ULID ULID identifiable test repository.
 */
@NoRepositoryBean
public interface UlidUlidIdentifiableRepository<T extends AbstractUlidUlidIdentifiable>
		extends UlidIdentifiableRepository<T, Ulid> {
}
