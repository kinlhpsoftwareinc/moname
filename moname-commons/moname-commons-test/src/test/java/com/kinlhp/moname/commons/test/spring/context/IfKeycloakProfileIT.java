package com.kinlhp.moname.commons.test.spring.context;

import lombok.NoArgsConstructor;

@IfKeycloakProfile
@NoArgsConstructor
final class IfKeycloakProfileIT extends AbstractIfProfileIT {

	@Override
	String expectedProfile() {
		return KEYCLOAK;
	}
}
