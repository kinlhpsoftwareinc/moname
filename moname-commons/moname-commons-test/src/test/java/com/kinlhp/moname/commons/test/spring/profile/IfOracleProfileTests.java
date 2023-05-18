package com.kinlhp.moname.commons.test.spring.profile;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;

// TODO: Remove Lombok.
@IfOracleProfile
@NoArgsConstructor(onConstructor_ = {@Nonnull})
final class IfOracleProfileTests extends AbstractIfProfileTests {

	@Nonnull
	@Override
	String expectedProfile() {
		return ORACLE;
	}
}
