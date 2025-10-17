package com.kinlhp.moname.commons.test.testcontainers.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.DisabledIf;

import com.kinlhp.moname.commons.test.spring.context.IfMySqlProfile;

import static com.kinlhp.moname.commons.test.testcontainers.keycloak.OptimizedKeycloakContainerIT.EXPRESSION;

@DisabledIf(
		expression = EXPRESSION,
		loadContext = true,
		reason = "Optimized Keycloak should only be tested with \"keycloak-optimized\" profile"
)
@IfMySqlProfile
class OptimizedKeycloakContainerIT<T extends ExtendableKeycloakContainer<T>>
		extends KeycloakContainerIT<T> {

	/**
	 * @see org.springframework.core.env.Profiles#of(String...) of
	 */
	public static final String EXPRESSION =
			"#{environment.acceptsProfiles(T(org.springframework.core.env.Profiles).of('!keycloak-optimized'))}";

	public OptimizedKeycloakContainerIT(final @Autowired T keycloak) {
		super(keycloak);
	}

	@Override
	void assertInstanceOf() {
		Assertions.assertInstanceOf(ExtendedKeycloakContainer.class, getKeycloak());
	}
}
