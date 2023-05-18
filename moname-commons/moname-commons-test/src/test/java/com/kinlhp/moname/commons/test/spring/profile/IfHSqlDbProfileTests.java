package com.kinlhp.moname.commons.test.spring.profile;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;

// TODO: Remove Lombok.
@IfHSqlDbProfile
@NoArgsConstructor(onConstructor_ = {@Nonnull})
final class IfHSqlDbProfileTests extends AbstractIfProfileTests {

	@Nonnull
	@Override
	String expectedProfile() {
		return HSQLDB;
	}
}
