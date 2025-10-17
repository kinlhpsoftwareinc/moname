package com.kinlhp.moname.commons.test.testcontainers.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import java.net.URI;
import java.util.Optional;

import com.kinlhp.moname.commons.test.testcontainers.UriExtractableContainer;

import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.DEFAULT_ISSUER_URI;

@SuppressWarnings("java:S119")
public class WrappedKeycloakContainer<SELF extends WrappedKeycloakContainer<SELF>>
		extends ExtendableKeycloakContainer<SELF>
		implements UriExtractableContainer<SELF> {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(WrappedKeycloakContainer.class);

	@Nonnull
	private URI issuerUri = DEFAULT_ISSUER_URI;

	@Nonnull
	public Optional<URI> getIssuerUri() {
		return Optional.of(issuerUri);
	}

	@Nonnull
	@Override
	public Logger getLogger() {
		return LOG;
	}

	@Nonnull
	@Override
	public Optional<URI> getUri() {
		return getIssuerUri();
	}

	@Nonnull
	public SELF withIssuerUri(@Nonnull final URI issuerUri) {
		LOG.warn("Replacing the current issuer URI {} with the new issuer URI {}", this.issuerUri, issuerUri);
		this.issuerUri = issuerUri;
		return super.self();
	}

	@Nonnull
	@Override
	public SELF withUri(@Nonnull final URI uri) {
		return withIssuerUri(uri);
	}
}
