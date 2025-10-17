package com.kinlhp.moname.commons.jpa.repository.ulid.string;

import org.springframework.data.repository.NoRepositoryBean;

import com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;

/**
 * String ULID identifiable test repository.
 */
@NoRepositoryBean
public interface StringUlidIdentifiableRepository<T extends AbstractStringUlidIdentifiable>
		extends UlidIdentifiableRepository<T, String> {
}
