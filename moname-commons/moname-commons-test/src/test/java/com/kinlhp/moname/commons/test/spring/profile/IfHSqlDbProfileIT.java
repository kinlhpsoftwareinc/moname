package com.kinlhp.moname.commons.test.spring.profile;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;

@IfHSqlDbProfile
@NoArgsConstructor
final class IfHSqlDbProfileIT extends AbstractIfProfileIT {

	@Nonnull
	@Override
	String expectedProfile() {
		return HSQLDB;
	}
}
