package com.kinlhp.moname.commons.test.spring.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import static com.kinlhp.moname.commons.test.spring.security.MockMvcHeaderCapturer.headerOf;

@DisplayName("@ClientCredentialsFlow without Keycloak in the application context")
@WebMvcTest
class ClientCredentialsFlowExtensionWithoutKeycloakIT {

	@ClientCredentialsFlow(clientId = "without-keycloak@moname")
	@DisplayName("resolves a MockMvc carrying an empty token")
	@Test
	final void resolvesMockMvcCarryingEmptyToken(final MockMvc resolvedMockMvc) {
		assertThat(headerOf(resolvedMockMvc, AUTHORIZATION)).isNull();
	}
}
