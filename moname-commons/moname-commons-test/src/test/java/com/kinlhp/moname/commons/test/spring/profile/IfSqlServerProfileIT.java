package com.kinlhp.moname.commons.test.spring.profile;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;

@IfSqlServerProfile
@NoArgsConstructor
final class IfSqlServerProfileIT extends AbstractIfProfileIT {

	@Nonnull
	@Override
	String expectedProfile() {
		return SQLSERVER;
	}
}
