package com.kinlhp.moname.commons.test.spring.profile;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;

// TODO: Remove Lombok.
@IfTestProfile
@NoArgsConstructor(onConstructor_ = {@Nonnull})
final class IfTestProfileTests extends AbstractIfProfileTests {

	@Nonnull
	@Override
	String expectedProfile() {
		return TEST;
	}
}
