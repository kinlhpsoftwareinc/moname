package com.kinlhp.moname.commons.test.spring.context;

import lombok.NoArgsConstructor;

import jakarta.annotation.Nonnull;

@IfKeycloakProfile
@NoArgsConstructor
final class IfKeycloakProfileIT extends AbstractIfProfileIT {

	@Nonnull
	@Override
	String expectedProfile() {
		return KEYCLOAK;
	}
}
