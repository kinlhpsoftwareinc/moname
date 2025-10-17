package com.kinlhp.moname.addressing.api.country;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kinlhp.moname.addressing.repository.Countries;

@org.springframework.stereotype.Repository
public interface Repository extends Countries, JpaRepository<com.kinlhp.moname.addressing.jpa.country.Entity, Integer> {

	@Override
	default long count() {
		return Countries.super.count();
	}
}
