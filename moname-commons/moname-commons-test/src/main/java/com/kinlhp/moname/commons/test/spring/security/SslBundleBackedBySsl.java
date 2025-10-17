package com.kinlhp.moname.commons.test.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.ssl.NoSuchSslBundleException;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerSslBundle;

import java.util.Objects;
import java.util.Optional;

public final class SslBundleBackedBySsl {

	private static final Logger LOG = LoggerFactory.getLogger(SslBundleBackedBySsl.class);

	private SslBundleBackedBySsl() {
	}

	public static SslBundle of(final ObjectProvider<ServerProperties> serverProperties,
			final ObjectProvider<SslBundles> sslBundles) {
		try {
			return sslBundles.stream()
					.flatMap(bundles -> ssl(serverProperties)
							.map(ssl -> WebServerSslBundle.get(ssl, bundles))
							.stream())
					.findFirst()
					.orElseGet(SslBundleBackedBySsl::systemDefaultSslBundle);
		} catch (final IllegalStateException | NoSuchSslBundleException exception) {
			exception.printStackTrace();
			return systemDefaultSslBundle();
		}
	}

	private static Optional<Ssl> ssl(final ObjectProvider<ServerProperties> serverProperties) {
		return serverProperties.stream()
				.map(ServerProperties::getSsl)
				.filter(Objects::nonNull)
				.filter(ssl -> ssl.isEnabled())
				.findFirst()
				.or(() -> {
					LOG.warn("No server SSL enabled was found");
					return Optional.empty();
				});
	}

	private static SslBundle systemDefaultSslBundle() {
		LOG.warn("No named SSLBundle was found; the system's default SSLBundle will be used");
		return SslBundle.systemDefault();
	}
}
