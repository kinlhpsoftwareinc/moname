package com.kinlhp.moname.commons.test.testcontainers.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.DisabledIf;

import jakarta.annotation.Nonnull;

import static com.kinlhp.moname.commons.test.testcontainers.keycloak.UnoptimizedExtendedKeycloakContainerIT.EXPRESSION;

@DisabledIf(
		expression = EXPRESSION,
		loadContext = true,
		reason = "Unoptimized Keycloak should only be tested with database profiles other than \"mysql\""
)
class UnoptimizedExtendedKeycloakContainerIT<T extends ExtendableKeycloakContainer<T>>
		extends ExtendedKeycloakContainerIT<T> {

	/**
	 * @see org.springframework.core.env.Profiles#of(String...) of
	 */
	@Nonnull
	public static final String EXPRESSION =
			"#{environment.acceptsProfiles(T(org.springframework.core.env.Profiles).of('mysql'))}";

	public UnoptimizedExtendedKeycloakContainerIT(@Autowired @Nonnull final T keycloak) {
		super(keycloak);
	}

	@Override
	void assertInstanceOf() {
		Assertions.assertInstanceOf(WrappedKeycloakContainer.class, getKeycloak());
	}
}
