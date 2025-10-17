//package com.kinlhp.moname.commons.test.spring.security;
//
//import java.net.URISyntaxException;
//import java.nio.file.Path;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import jakarta.annotation.Nonnull;
//import jakarta.annotation.Nullable;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.KEYCLOAK;
//import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.MONAME;
//
//// TODO: Alterar esse nome, porque o profile unoptimized_keycloak nao usa TLS
//public abstract class AbstractTlsClientCredentialsFlowTests /*extends AbstractTokenUriSupplier */{
//
//	@Nonnull
//	private static final Logger LOG = LoggerFactory.getLogger(AbstractTlsClientCredentialsFlowTests.class);
//
//	@BeforeAll
//	static void beforeAll() {
//		// TODO: Make it work with configured bundle (server.ssl.bundle in application-security.yaml)
//		System.getProperties()
//				.entrySet().stream()
//				.filter(AbstractTlsClientCredentialsFlowTests::isOptimizedKeycloak)
//				.findFirst()
//				.ifPresent(AbstractTlsClientCredentialsFlowTests::setTlsProperties);
//	}
//
//	private static boolean isOptimizedKeycloak(@Nonnull final Entry<Object, Object> property) {
//		return Map.entry("keycloak.profile.active", "keycloak-optimized").equals(property);
//		//return "keycloak.profile.active".equals(property.getKey())
//		//		&& "keycloak-optimized".equals(property.getValue());
//	}
//
//	private static void setTlsProperties(@Nonnull final Entry<Object, Object> property) {
//		LOG.info("Setting TLS properties because optimized Keycloak profile is active");
//		//System.setProperty("javax.net.debug", "ssl:handshake:verbose");
//		System.setProperty("javax.net.ssl.trustStore", getTrustStorePath());
//		System.setProperty("javax.net.ssl.trustStorePassword", MONAME);
//		System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
//	}
//
//	@Nonnull
//	private static String getTrustStorePath() {
//		@Nonnull final var resourceName = "/oidc/%s/tls/keystore.ed25519.p12".formatted(KEYCLOAK);
//		@Nullable final var trustStore = AbstractTlsClientCredentialsFlowTests.class.getResource(resourceName);
//		try {
//			//noinspection DataFlowIssue
//			return Path.of(trustStore.toURI()).toString();
//		} catch (@Nonnull final URISyntaxException exception) {
//			LOG.error("// TODO: Log", exception); // TODO: Log
//			throw new RuntimeException("// TODO: Message", exception); // TODO: Message
//		}
//	}
//
//	//protected AbstractTlsClientCredentialsFlowTests(@Nonnull final URI tokenUri) {
//	//	super(tokenUri);
//	//}
//}
