package com.kinlhp.moname.commons.test.spring.profile;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;

@IfOracleProfile
@NoArgsConstructor
final class IfOracleProfileIT extends AbstractIfProfileIT {

	@Nonnull
	@Override
	String expectedProfile() {
		return ORACLE;
	}
}
