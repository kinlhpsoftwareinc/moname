package com.kinlhp.moname.commons.jpa.id.ulid;

import java.util.Optional;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator;

/**
 * <a href=https://github.com/f4b6a3/ulid-creator#hash-ulid>Hash ULID</a>
 *
 * @see com.github.f4b6a3.ulid.UlidCreator#getHashUlid(long, String) getHashUlid
 */
public interface HashableUlid {

	default Optional<UlidGenerator> getHashConfig() {
		return Optional.empty();
	}
}
