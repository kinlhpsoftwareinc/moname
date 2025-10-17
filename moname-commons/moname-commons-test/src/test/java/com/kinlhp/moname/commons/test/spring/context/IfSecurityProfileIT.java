package com.kinlhp.moname.commons.test.spring.context;

import lombok.NoArgsConstructor;

@IfSecurityProfile
@NoArgsConstructor
final class IfSecurityProfileIT extends AbstractIfProfileIT {

	@Override
	String expectedProfile() {
		return SECURITY;
	}
}
