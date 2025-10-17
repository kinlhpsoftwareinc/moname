// package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;
//
// import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
// import dasniko.testcontainers.keycloak.KeycloakContainer;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.SmartInitializingSingleton;
// import org.springframework.boot.autoconfigure.AutoConfiguration;
// import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Lazy;
// import org.springframework.context.annotation.Profile;
// import org.springframework.core.env.ConfigurableEnvironment;
// import org.testcontainers.containers.GenericContainer;
// import org.testcontainers.containers.MySQLContainer;
//
// import jakarta.annotation.Nonnull;
// import jakarta.annotation.PostConstruct;
// import java.net.URI;
//
// import com.kinlhp.moname.commons.test.spring.security.configuration.OAuth2ClientProviderProperties;
// import com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer;
//
// import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.KC_HTTP_RELATIVE_PATH;
// import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.KEYCLOAK;
// import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.MONAME;
// import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.REALM_RESOURCE_NAME_CLASSPATH;
// import static com.kinlhp.moname.commons.test.testcontainers.mysql.ExtendedMySQLContainer.NETWORK;
//
// @AutoConfiguration
// @Profile("test & security")
// public class KeycloakTestcontainersAutoConfiguration {
//
// 	@Nonnull
// 	private static final Logger LOG = LoggerFactory.getLogger(KeycloakTestcontainersAutoConfiguration.class);
//
// 	@PostConstruct
// 	public void postConstruct() {
// 		LOG.debug("Autoconfiguring Keycloak Testcontainers");
// 	}
//
// 	@Bean({
// 			"dasniko.testcontainers.keycloak.ExtendableKeycloakContainer",
// 			"com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer",
// 			"optimizedKeycloakContainer",
// 			KEYCLOAK
// 	})
// 	@Nonnull
// 	//@Profile("keycloak & mysql")
// 	@Profile("keycloak & mysql & keycloak-optimized")
// 	@ServiceConnection
// 	@SuppressWarnings({"unused", "java:S2095"})
// 	<T extends ExtendableKeycloakContainer<T>, S extends MySQLContainer<S>> GenericContainer<T> optimizedKeycloakContainer(
// 			/*// TODO: The database testcontainer is not a bean in the Spring context*/@Lazy @Nonnull final GenericContainer<S> mysql,
// 			@Nonnull final OAuth2ClientProviderProperties providerProperties) {
// 		LOG.debug("Manually starting optimized Keycloak container");
// 		@Nonnull final var keycloak = new ExtendedKeycloakContainer(self ->
// 				reconfigureOptimizedKeycloakContainer(self, mysql));
// 		keycloak.start();
// 		//tokenUriRegistrar(keycloak, providerProperties);
// 		// TODO: Mover para ca as configuracoes de javax.net.ssl (AbstractTlsClientCredentialsFlowTests)
// 		//noinspection unchecked
// 		return (GenericContainer<T>) keycloak;
// 	}
//
// 	@Bean({
// 			"dasniko.testcontainers.keycloak.ExtendableKeycloakContainer",
// 			"dasniko.testcontainers.keycloak.KeycloakContainer",
// 			"unoptimizedKeycloakContainer",
// 			KEYCLOAK
// 	})
// 	@Nonnull
// 	//@Profile("keycloak & !mysql)")
// 	@Profile("keycloak & !keycloak-optimized)")
// 	@ServiceConnection
// 	@SuppressWarnings("unused")
// 	<T extends ExtendableKeycloakContainer<T>> GenericContainer<T> unoptimizedKeycloakContainer(
// 			@Nonnull final OAuth2ClientProviderProperties providerProperties) {
// 		LOG.debug("Manually starting unoptimized Keycloak container");
// 		@Nonnull final var keycloak = createUnoptimizedKeycloakContainer();
// 		keycloak.start();
// 		//tokenUriRegistrar((GenericContainer<T>) keycloak, providerProperties);
// 		//noinspection unchecked
// 		return (GenericContainer<T>) keycloak;
// 	}
//
// 	private <T extends ExtendableKeycloakContainer<T>, S extends MySQLContainer<S>> void reconfigureOptimizedKeycloakContainer(
// 			@Nonnull final GenericContainer<T> keycloak, @Nonnull final GenericContainer<S> mysql) {
// 		LOG.debug("Reconfiguring optimized Keycloak container to reach MySQL container");
// 		// TODO: The database testcontainer is not a bean in the Spring context
// 		//@Nonnull final var kcDbUrlHost = mysql.getContainerName();
// 		//@Nonnull final var kcDbUrlPort = mysql.getMappedPort(MYSQL_PORT);
// 		//keycloak.withEnv("KC_DB_URL_HOST", kcDbUrlHost)
// 		//		.withEnv("KC_DB_URL_PORT", kcDbUrlPort.toString())
// 		//		.withNetwork(mysql.getNetwork());
// 		keycloak.withNetwork(NETWORK);
// 	}
//
// 	@Nonnull
// 	private <T extends ExtendableKeycloakContainer<T>> GenericContainer<T> createUnoptimizedKeycloakContainer() {
// 		@Nonnull @SuppressWarnings("java:S2095") final var keycloak = new KeycloakContainer()
// 				.withEnv("KC_BOOTSTRAP_ADMIN_PASSWORD", MONAME)
// 				.withEnv("KC_BOOTSTRAP_ADMIN_USERNAME", MONAME)
// 				.withContextPath(KC_HTTP_RELATIVE_PATH)
// 				.withRealmImportFile(REALM_RESOURCE_NAME_CLASSPATH);
// 		//noinspection unchecked
// 		return (GenericContainer<T>) keycloak;
// 	}
//
// 	@Bean
// 	public KeycloakConnectionDetailsBeanPostProcessor keycloakConnectionDetailsBeanPostProcessor(final ConfigurableEnvironment environment/* , final DynamicPropertyRegistry registry */) {
// 		return new KeycloakConnectionDetailsBeanPostProcessor(environment/* , registry */);
// 	}
//
// 	@Bean
// 	public <T extends ExtendableKeycloakContainer<T>> SmartInitializingSingleton postProcess(
// 			@Nonnull final GenericContainer<T> keycloak,
// 			@Nonnull final OAuth2ClientProviderProperties providerProperties) {
// 		return () -> postProcessIssuerUri(keycloak, providerProperties);
// 	}
//
// 	private <T extends ExtendableKeycloakContainer<T>> void postProcessIssuerUri(
// 			@Nonnull final GenericContainer<T> keycloak,
// 			@Nonnull final OAuth2ClientProviderProperties providerProperties) {
// 		@Nonnull @SuppressWarnings("unchecked") final var authUri = ((T) keycloak).getAuthServerUrl();
// 		@Nonnull final var issuerUri = "%s/realms/%s".formatted(authUri, MONAME);
// 		LOG.debug("""
// 						Post-processing Keycloak container: Dynamically registering properties \
// 						${spring.security.oauth2.resourceserver.jwt.issuer-uri}, \
// 						${spring.security.oauth2.resourceserver.jwt.jwk-set-uri} and \
// 						${spring.security.oauth2.client.provider.keycloak.token-uri} in {} with URIs related to {}""",
// 				providerProperties.getClass().getName(), issuerUri);
// 		providerProperties.setIssuerUri(URI.create(issuerUri));
// 	}
// }
