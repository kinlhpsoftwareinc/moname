package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.annotation.Nonnull;
import java.net.URI;
import java.util.Optional;

import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.JWKS_PATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.KC_HTTP_RELATIVE_PATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.REALM_PATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.TOKEN_ENDPOINT_PATH;

/**
 * @see dasniko.testcontainers.keycloak.ExtendableKeycloakContainer#getAuthServerUrl() getAuthServerUrl
 */
public record KeycloakWellKnownEndpoints(@Nonnull URI issuerUri, @Nonnull URI jwkSetUri, @Nonnull URI tokenUri) {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(KeycloakWellKnownEndpoints.class);

	public KeycloakWellKnownEndpoints {
		checkRequiredUri(issuerUri, "issuer URI");
		checkRequiredUri(jwkSetUri, "JWKS URI");
		checkRequiredUri(tokenUri, "token URI");
	}

	@Nonnull
	public static KeycloakWellKnownEndpoints from(@Nonnull final URI authServerUri) {
		checkRequiredUri(authServerUri, "Keycloak's auth server URI");
		LOG.debug("Deriving Keycloak well-known endpoints from auth server URI: {}", authServerUri);
		@Nonnull final var issuerUri = UriComponentsBuilder.fromUri(authServerUri)
				.replacePath(KC_HTTP_RELATIVE_PATH)
				.path(REALM_PATH)
				.replaceQuery(null)
				.fragment(null);
		return new KeycloakWellKnownEndpoints(
				issuerUri.build().toUri(),
				((UriComponentsBuilder) issuerUri.clone()).path(JWKS_PATH).build().toUri(),
				((UriComponentsBuilder) issuerUri.clone()).path(TOKEN_ENDPOINT_PATH).build().toUri());
	}

	private static void checkRequiredUri(@Nonnull final URI uri, @Nonnull final String uriDescription) {
		//noinspection OptionalOfNullableMisuse
		Optional.ofNullable(uri)
				.filter(notNullUri -> !notNullUri.toString().isBlank())
				.filter(notBlankUri -> !notBlankUri.equals(URI.create("about:blank")))
				.orElseThrow(() -> new IllegalArgumentException("%s must not be null, blank, or about:blank"
						.formatted(uriDescription)));
	}
}
