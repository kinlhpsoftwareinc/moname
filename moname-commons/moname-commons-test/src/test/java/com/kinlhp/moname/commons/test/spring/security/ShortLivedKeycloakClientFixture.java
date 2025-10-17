package com.kinlhp.moname.commons.test.spring.security;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ssl.NoSuchSslBundleException;
import org.springframework.boot.ssl.SslBundles;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response.Status;
import javax.net.ssl.SSLContext;
import java.util.Map;
import java.util.Optional;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MONAME;

/**
 * Registers, through the Keycloak Admin REST API, a client whose access tokens expire in 1 second, and removes it on
 * {@link #close() close}.
 */
final class ShortLivedKeycloakClientFixture implements AutoCloseable {

	static final String CLIENT_ID = "moname-commons-api-short-lived";

	static final String CLIENT_SECRET = MONAME;

	private static final Logger LOG = LoggerFactory.getLogger(ShortLivedKeycloakClientFixture.class);

	private static final String ACCESS_TOKEN_LIFESPAN_SECONDS = "1";

	private static final String ADMIN_CLIENT_ID = "admin-cli";

	private static final String ADMIN_REALM = "master";

	private static final String ADMIN_USERNAME_ENV = "KC_BOOTSTRAP_ADMIN_USERNAME";

	private static final String ADMIN_PASSWORD_ENV = "KC_BOOTSTRAP_ADMIN_PASSWORD";

	private static final String SSL_BUNDLE = "ed25519-p12";
	//private static final String SSL_BUNDLE = "rsa-p12";

	private final Keycloak admin;

	private ShortLivedKeycloakClientFixture(final Keycloak admin) {
		this.admin = admin;
	}

	static ShortLivedKeycloakClientFixture register(final ExtendableKeycloakContainer<?> keycloak,
			final SslBundles sslBundles) {
		LOG.debug("Registering the client '{}' in realm '{}' with access tokens expiring in {} second(s)",
				CLIENT_ID, MONAME, ACCESS_TOKEN_LIFESPAN_SECONDS);
		final var shortLivedClient = new ShortLivedKeycloakClientFixture(adminOf(keycloak, sslBundles));
		try {
			shortLivedClient.removeExisting();
			try (final var response = shortLivedClient.clients().create(representation())) {
				if (response.getStatus() != Status.CREATED.getStatusCode()) {
					final var message = "The client '%s' could not be registered: HTTP %d %s"
							.formatted(CLIENT_ID, response.getStatus(), response.readEntity(String.class));
					LOG.error(message);
					throw new IllegalStateException(message);
				}
			}
			LOG.debug("The client '{}' was registered in realm '{}'", CLIENT_ID, MONAME);
			return shortLivedClient;
		} catch (final RuntimeException exception) {
			LOG.error("Closing the Keycloak admin client since the client '{}' could not be registered", CLIENT_ID,
					exception);
			shortLivedClient.admin.close();
			throw exception;
		}
	}

	@Override
	public void close() {
		LOG.debug("Removing the client '{}' from realm '{}'", CLIENT_ID, MONAME);
		try {
			removeExisting();
		} finally {
			admin.close();
			LOG.trace("Keycloak admin client closed");
		}
	}

	/**
	 * Keeps registration idempotent when a previous run left the client behind (e.g. reused containers).
	 */
	private void removeExisting() {
		final var existing = clients().findByClientId(CLIENT_ID);
		if (existing.isEmpty()) {
			LOG.trace("There is no client '{}' to be removed from realm '{}'", CLIENT_ID, MONAME);
			return;
		}
		existing.forEach(client -> {
			clients().get(client.getId()).remove();
			LOG.debug("The client '{}' (id {}) was removed from realm '{}'", CLIENT_ID, client.getId(), MONAME);
		});
	}

	private ClientsResource clients() {
		return admin.realm(MONAME).clients();
	}

	private static ClientRepresentation representation() {
		final var client = new ClientRepresentation();
		client.setClientId(CLIENT_ID);
		client.setSecret(CLIENT_SECRET);
		client.setEnabled(true);
		client.setPublicClient(false);
		client.setServiceAccountsEnabled(true);
		client.setStandardFlowEnabled(false);
		client.setDirectAccessGrantsEnabled(false);
		client.setProtocol("openid-connect");
		client.setAttributes(Map.of("access.token.lifespan", ACCESS_TOKEN_LIFESPAN_SECONDS));
		LOG.trace("Client representation: clientId '{}', protocol '{}', attributes {}",
				client.getClientId(), client.getProtocol(), client.getAttributes());
		return client;
	}

	/**
	 * Admin credentials are read from the container environment because both factories set the bootstrap admin through
	 * {@code KC_BOOTSTRAP_ADMIN_*}, which {@link ExtendableKeycloakContainer#getAdminUsername()} does not reflect in
	 * the optimized container.
	 */
	private static Keycloak adminOf(final ExtendableKeycloakContainer<?> keycloak, final SslBundles sslBundles) {
		final var env = keycloak.getEnvMap();
		final var username = env.getOrDefault(ADMIN_USERNAME_ENV, keycloak.getAdminUsername());
		final var httpClient = ClientBuilder.newBuilder();
		sslContextOf(sslBundles).ifPresent(httpClient::sslContext);
		LOG.trace("Creating a Keycloak admin client for user '{}' (realm '{}', client '{}') pointing to {}",
				username, ADMIN_REALM, ADMIN_CLIENT_ID, keycloak.getAuthServerUrl());
		return KeycloakBuilder.builder()
				.serverUrl(keycloak.getAuthServerUrl())
				.realm(ADMIN_REALM)
				.clientId(ADMIN_CLIENT_ID)
				.username(username)
				.password(env.getOrDefault(ADMIN_PASSWORD_ENV, keycloak.getAdminPassword()))
				.resteasyClient(httpClient.build())
				.build();
	}

	/**
	 * Same SSL bundle as {@link ClientCredentialsFlowExtension ClientCredentialsFlowExtension}; absent in the
	 * unoptimized profile, which serves plain HTTP.
	 */
	private static Optional<SSLContext> sslContextOf(final SslBundles sslBundles) {
		try {
			final var sslContext = sslBundles.getBundle(SSL_BUNDLE).createSslContext();
			LOG.trace("The Keycloak admin client uses the SSL bundle '{}'", SSL_BUNDLE);
			return Optional.of(sslContext);
		} catch (final NoSuchSslBundleException exception) {
			LOG.warn("There is no SSL bundle '{}', so the Keycloak admin client uses the default SSL context",
					SSL_BUNDLE);
			return Optional.empty();
		}
	}
}
