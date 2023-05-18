package com.kinlhp.moname.commons.test.spring.profile;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;

// TODO: Remove Lombok.
@IfSqlServerProfile
@NoArgsConstructor(onConstructor_ = {@Nonnull})
final class IfSqlServerProfileTests extends AbstractIfProfileTests {

	@Nonnull
	@Override
	String expectedProfile() {
		return SQLSERVER;
	}
}
