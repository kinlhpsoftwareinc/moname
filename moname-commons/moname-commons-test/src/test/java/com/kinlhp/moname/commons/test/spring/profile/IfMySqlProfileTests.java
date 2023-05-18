package com.kinlhp.moname.commons.test.spring.profile;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;

// TODO: Remove Lombok.
@IfMySqlProfile
@NoArgsConstructor(onConstructor_ = {@Nonnull})
final class IfMySqlProfileTests extends AbstractIfProfileTests {

	@Nonnull
	@Override
	String expectedProfile() {
		return MYSQL;
	}
}
