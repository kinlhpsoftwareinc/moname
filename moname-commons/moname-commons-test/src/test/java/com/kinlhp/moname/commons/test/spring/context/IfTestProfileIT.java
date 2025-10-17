package com.kinlhp.moname.commons.test.spring.context;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;

@IfTestProfile
@NoArgsConstructor
final class IfTestProfileIT extends AbstractIfProfileIT {

	@Nonnull
	@Override
	String expectedProfile() {
		return TEST;
	}
}
