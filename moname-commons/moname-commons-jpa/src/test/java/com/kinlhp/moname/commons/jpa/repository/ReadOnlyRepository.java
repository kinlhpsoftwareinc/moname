package com.kinlhp.moname.commons.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kinlhp.moname.commons.jpa.entity.ReadOnlyEntity;

/**
 * Read-only test repository.
 */
@Repository
public interface ReadOnlyRepository extends JpaRepository<ReadOnlyEntity, Integer> {
}
