package com.kinlhp.moname.commons.test.spring.context;

import lombok.NoArgsConstructor;

import jakarta.annotation.Nonnull;

@IfSecurityProfile
@NoArgsConstructor
final class IfSecurityProfileIT extends AbstractIfProfileIT {

	@Nonnull
	@Override
	String expectedProfile() {
		return SECURITY;
	}
}
