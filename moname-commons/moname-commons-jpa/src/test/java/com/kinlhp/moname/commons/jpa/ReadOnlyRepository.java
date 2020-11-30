package com.kinlhp.moname.commons.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Read-only test repository.
 */
@Repository
interface ReadOnlyRepository extends CrudRepository<ReadOnlyEntity, Integer> {
}
