package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Nonnull;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("KeycloakEndpoints")
class KeycloakWellKnownEndpointsTest {

	@Nonnull
	private static final URI AUTH_SERVER_URI = URI.create("https://keycloak:8443/to-be-replaced");

	@DisplayName("derives issuer, JWKS, and token URIs from the auth server URI")
	@Test
	final void derivesWellKnownUrisFromAuthServerUri() {
		@Nonnull final var wellKnownEndpoints = KeycloakWellKnownEndpoints.from(AUTH_SERVER_URI);
		assertThat(wellKnownEndpoints.issuerUri()).isEqualTo(URI.create("https://keycloak:8443/auth/realms/moname"));
		assertThat(wellKnownEndpoints.jwkSetUri())
				.isEqualTo(URI.create("https://keycloak:8443/auth/realms/moname/protocol/openid-connect/certs"));
		assertThat(wellKnownEndpoints.tokenUri())
				.isEqualTo(URI.create("https://keycloak:8443/auth/realms/moname/protocol/openid-connect/token"));
	}

	@DisplayName("produces identical URIs whether or not the auth server URI carries a trailing slash")
	@Test
	final void trailingSlashIsIgnored() {
		@Nonnull final var withoutTrailingSlash = KeycloakWellKnownEndpoints.from(AUTH_SERVER_URI);
		@Nonnull final var withTrailingSlash = KeycloakWellKnownEndpoints
				.from(URI.create("%s/".formatted(AUTH_SERVER_URI)));
		assertThat(withTrailingSlash).isEqualTo(withoutTrailingSlash);
	}

	@DisplayName("rejects a null auth server URI")
	@Test
	final void rejectsNullAuthServerUri() {
		//noinspection DataFlowIssue
		rejectsAuthServerUri(null);
	}

	@DisplayName("rejects an empty auth server URI")
	@Test
	final void rejectsEmptyAuthServerUri() {
		rejectsAuthServerUri(URI.create(""));
	}

	@DisplayName("rejects a blank auth server URI")
	@Test
	final void rejectsAboutBlankAuthServerUri() {
		rejectsAuthServerUri(URI.create("about:blank"));
	}

	private void rejectsAuthServerUri(@Nonnull final URI authServerUri) {
		assertThatThrownBy(() -> KeycloakWellKnownEndpoints.from(authServerUri))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Keycloak's auth server URI must not be null, blank, or about:blank");
	}
}
