package com.kinlhp.moname.commons.test.spring.context;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;

@IfMySqlProfile
@NoArgsConstructor
final class IfMySqlProfileIT extends AbstractIfProfileIT {

	@Nonnull
	@Override
	String expectedProfile() {
		return MYSQL;
	}
}
