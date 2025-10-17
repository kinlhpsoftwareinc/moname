package com.kinlhp.moname.commons.jpa.repository.ulid;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.kinlhp.moname.commons.jpa.entity.Persistable;

/**
 * ULID identifiable test repository.
 */
@NoRepositoryBean
@SuppressWarnings("java:S119")
public interface UlidIdentifiableRepository<T extends Persistable<PK>, PK extends Serializable>
		extends JpaRepository<T, PK> {
}
