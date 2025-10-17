package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.testcontainers.containers.MySQLContainer;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.util.function.Supplier;

import com.kinlhp.moname.commons.test.spring.security.autoconfigure.ssl.JwtDecoderSslBundleConfiguration;
import com.kinlhp.moname.commons.test.testcontainers.SharedNetwork;
import com.kinlhp.moname.commons.test.testcontainers.UriExtractableContainer;
import com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer;
import com.kinlhp.moname.commons.test.testcontainers.keycloak.WrappedKeycloakContainer;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.KEYCLOAK;
import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MONAME;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.JWKS_PATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.KC_HTTP_RELATIVE_PATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.REALM_PATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.REALM_RESOURCE_NAME_CLASSPATH;
import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.TOKEN_ENDPOINT_PATH;

/**
 * @see org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration KafkaAutoConfiguration
 */
@AutoConfiguration
@ConditionalOnClass(ExtendableKeycloakContainer.class)
@DependsOnDatabaseInitialization
@EnableConfigurationProperties({OAuth2ResourceServerProperties.class, OAuth2ClientProperties.class})
@Import(JwtDecoderSslBundleConfiguration.class) // TODO: To be removed
@Profile("test & security")
public class KeycloakAutoConfiguration implements PriorityOrdered {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(KeycloakAutoConfiguration.class);

	@SuppressWarnings("java:S3077")
	private static volatile ExtendableKeycloakContainer<?> singleton = null;

	@Nonnull
	private static <T extends ExtendableKeycloakContainer<T>> T getSingleton(@Nonnull final Supplier<T> keycloak) {
		if (singleton == null) {
			synchronized (ExtendableKeycloakContainer.class) {
				if (singleton == null) {
					singleton = keycloak.get();
				}
			}
		}
		//noinspection unchecked
		return (T) singleton;
	}

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Autoconfiguring Keycloak Testcontainers");
	}

	/**
	 * @see org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetailsBeanPostProcessor#getOrder() getOrder
	 */
	@Override
	public int getOrder() {
		// Runs after JdbcConnectionDetailsBeanPostProcessor
		return Ordered.HIGHEST_PRECEDENCE + 3;
	}

	@Bean
	@ConditionalOnMissingBean(KeycloakConnectionDetails.class)
	@Nonnull
	<T extends ExtendableKeycloakContainer<T>> PropertiesKeycloakConnectionDetails keycloakConnectionDetails(
			@Nonnull final OAuth2ResourceServerProperties resourceServerProperties,
			@Nonnull final OAuth2ClientProperties clientProperties,
			@Nonnull final ObjectProvider<SslBundles> sslBundles) {
		return new PropertiesKeycloakConnectionDetails(resourceServerProperties, clientProperties,
				sslBundles.getIfAvailable());
	}

	@Bean({
			"dasniko.testcontainers.keycloak.ExtendableKeycloakContainer",
			"com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer",
			"optimizedKeycloakContainer",
			KEYCLOAK
	})
	@Nonnull
	@Profile("mysql & keycloak-optimized")
	<T extends ExtendableKeycloakContainer<T>, S extends MySQLContainer<S>> T optimizedKeycloakContainer(
			/*// TODO: The database testcontainer is not a bean in the Spring context*/@Lazy @Nonnull final S mysql,
			//@Nonnull final OAuth2ResourceServerProperties resourceServerProperties,
			//@Nonnull final OAuth2ClientProperties clientProperties) {
			@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
		//noinspection unchecked
		@Nonnull final var keycloak = getSingleton(() ->
				(T) createOptimizedKeycloakContainer(mysql, keycloakConnectionDetails));
		LOG.debug("Starting optimized Keycloak container");
		keycloak.start();
		// TODO: Move to here the javax.net.ssl configurations (AbstractTlsClientCredentialsFlowTests)
		//mapProperties(keycloak, resourceServerProperties, clientProperties);
		mapProperties(keycloak, keycloakConnectionDetails);
		return keycloak;
	}

	@Nonnull
	private <T extends ExtendableKeycloakContainer<T>, S extends MySQLContainer<S>> T createOptimizedKeycloakContainer(
			@Nonnull final S mysql, @Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
		@Nullable T keycloak;
		synchronized (ExtendedKeycloakContainer.class) {
			//noinspection unchecked
			keycloak = (T) new ExtendedKeycloakContainer<>(
					self -> reconfigureKeycloakContainerMemory(self, keycloakConnectionDetails),
					//self -> ((T) self).withCreateContainerCmdModifier(createContainerCmd -> reconfigureKeycloakContainerMemory(createContainerCmd, keycloakConnectionDetails)),
					//self -> reconfigureKeycloakContainerNetwork((T) self, mysql)
					self -> {
						LOG.debug("""
								Reconfiguring optimized Keycloak container to reach MySQL container on the same network
								""");
						reconfigureKeycloakContainerNetwork(self);
					}
			);
		}
		return keycloak;
	}

	private <T extends ExtendableKeycloakContainer<T>> void reconfigureKeycloakContainerMemory(
			@Nonnull final T keycloak, @Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
		if (keycloak instanceof @Nonnull final UriExtractableContainer<?> self) {
			@Nonnull final var issuerUri = keycloakConnectionDetails.getIssuerUri();
			LOG.debug("Reconfiguring Keycloak container memory based on the issuer URI {}", issuerUri);
			self.withUri(issuerUri).withAutoResolveTcPatternUriParameters();
		}
	}

	//private void reconfigureKeycloakContainerMemory(@Nonnull final CreateContainerCmd createContainerCmd,
	//		@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
	//	optionalHostConfigOf(createContainerCmd).ifPresentOrElse(
	//			hostConfig -> doReconfigureKeycloakContainerMemory(hostConfig, keycloakConnectionDetails),
	//			() -> LOG.warn("HostConfig is null, cannot reconfigure optimized Keycloak container memory")
	//	);
	//}

	//private void doReconfigureKeycloakContainerMemory(@Nonnull final HostConfig hostConfig,
	//		@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
	//	LOG.debug("Reconfiguring optimized Keycloak container memory: [mem: 2GB | swap: 0B | shm: 0B]");
	//	// TODO: Use `application.yaml` and `@DataSizeUnit(BYTES) @Nonnull DataSize memory`
	//	//hostConfig.withMemory(2L * 1024L * 1024L * 1024L) // 2GB
	//	//		.withMemorySwap(0L)
	//	//		.withShmSize(0L);
	//	hostConfig.withMemory(keycloakConnectionDetails.getIssuerUri().getMemory());
	//}

	//@Nonnull
	//private Optional<HostConfig> optionalHostConfigOf(@Nonnull final CreateContainerCmd createContainerCmd) {
	//	return Optional.ofNullable(createContainerCmd.getHostConfig());
	//}

	//private <T extends ExtendableKeycloakContainer<T>, S extends MySQLContainer<S>> void reconfigureKeycloakContainerNetwork(
	//		@Nonnull final T keycloak, @Nonnull final S mysql) {
	//	LOG.debug("Reconfiguring optimized Keycloak container to reach MySQL container at network {}",
	//			SharedNetwork.getSingleton().getNetworkName());
	//	// TODO: The database testcontainer is not a bean in the Spring context
	//	@Nonnull final var kcDbUrlHost = mysql.getContainerName();
	//	@Nonnull final var kcDbUrlPort = mysql.getMappedPort(MYSQL_PORT);
	//	keycloak.withEnv("KC_DB_URL_HOST", kcDbUrlHost)
	//			.withEnv("KC_DB_URL_PORT", kcDbUrlPort.toString())
	//			.withNetwork(mysql.getNetwork());
	//}

	private <T extends ExtendableKeycloakContainer<T>> void reconfigureKeycloakContainerNetwork(
			@Nonnull final T keycloak) {
		@Nonnull final var network = SharedNetwork.getSingleton();
		LOG.debug("Reconfiguring Keycloak container to reach other containers on network {}", network.getNetworkName());
		keycloak.withNetwork(network.getNetwork());
	}

	@Bean({
			"dasniko.testcontainers.keycloak.ExtendableKeycloakContainer",
			"dasniko.testcontainers.keycloak.KeycloakContainer",
			"unoptimizedKeycloakContainer",
			KEYCLOAK
	})
	@Nonnull
	@Profile("!(mysql & keycloak-optimized)")
	<T extends ExtendableKeycloakContainer<T>> T unoptimizedKeycloakContainer(
			//@Nonnull final OAuth2ResourceServerProperties resourceServerProperties,
			//@Nonnull final OAuth2ClientProperties clientProperties) {
			@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
		//noinspection unchecked
		@Nonnull @SuppressWarnings("RedundantCast") final T keycloak = getSingleton(() ->
				(T) createUnoptimizedKeycloakContainer(keycloakConnectionDetails));
		LOG.debug("Starting unoptimized Keycloak container");
		keycloak.start();
		//mapProperties(keycloak, resourceServerProperties, clientProperties);
		mapProperties(keycloak, keycloakConnectionDetails);
		return keycloak;
	}

	@Nonnull
	@SuppressWarnings("java:S2095")
	private <T extends ExtendableKeycloakContainer<T>> T createUnoptimizedKeycloakContainer(
			@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
		@Nullable T keycloak;
		synchronized (WrappedKeycloakContainer.class) {
			//noinspection resource,unchecked
			keycloak = (T) new WrappedKeycloakContainer<>()
					.withEnv("KC_BOOTSTRAP_ADMIN_PASSWORD", MONAME)
					.withEnv("KC_BOOTSTRAP_ADMIN_USERNAME", MONAME)
					.withContextPath(KC_HTTP_RELATIVE_PATH)
					.withRealmImportFile(REALM_RESOURCE_NAME_CLASSPATH);
					//.withNetwork(SharedNetwork.getSingleton().getNetwork());
			reconfigureKeycloakContainerMemory(keycloak, keycloakConnectionDetails);
			reconfigureKeycloakContainerNetwork(keycloak);
		}
		return keycloak;
	}

	private <T extends ExtendableKeycloakContainer<T>> void mapProperties(@Nonnull final T keycloak,
			//@Nonnull final OAuth2ResourceServerProperties resourceServerProperties,
			//@Nonnull final OAuth2ClientProperties clientProperties) {
			@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
		LOG.debug("""
						Post-processing Keycloak container startup: Mapping from Keycloak container URI {} to Spring \
						Security properties ${spring.security.oauth2.resourceserver.jwt.issuer-uri}, \
						${spring.security.oauth2.resourceserver.jwt.jwk-set-uri} in {} and \
						${spring.security.oauth2.client.provider.keycloak.token-uri} in {}""",
				getIssuerUri(keycloak), OAuth2ResourceServerProperties.class.getName(),
				OAuth2ClientProperties.class.getName());
		@Nonnull final var propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
		//propertyMapper.from(getIssuerUri(keycloak).toString()).to(resourceServerProperties.getJwt()::setIssuerUri);
		//propertyMapper.from(getJwksUri(keycloak).toString()).to(resourceServerProperties.getJwt()::setJwkSetUri);
		//propertyMapper.from(getTokenEndpointUri(keycloak)).to(tokenUri -> clientProperties.getProvider().get(KEYCLOAK).setTokenUri(tokenUri));
		propertyMapper.from(getIssuerUri(keycloak)).to(keycloakConnectionDetails::setIssuerUri);
		propertyMapper.from(getJwksUri(keycloak)).to(keycloakConnectionDetails::setJwkSetUri);
		propertyMapper.from(getTokenEndpointUri(keycloak)).to(keycloakConnectionDetails::setTokenUri);
	}

	@Nonnull
	private <T extends ExtendableKeycloakContainer<T>> URI getIssuerUri(@Nonnull final T keycloak) {
		@Nonnull final var authUri = keycloak.getAuthServerUrl();
		return URI.create("%s%s".formatted(authUri, REALM_PATH));
	}

	@Nonnull
	private <T extends ExtendableKeycloakContainer<T>> URI getJwksUri(@Nonnull final T keycloak) {
		return URI.create("%s%s".formatted(getIssuerUri(keycloak), JWKS_PATH));
	}

	@Nonnull
	private <T extends ExtendableKeycloakContainer<T>> URI getTokenEndpointUri(@Nonnull final T keycloak) {
		return URI.create("%s%s".formatted(getIssuerUri(keycloak), TOKEN_ENDPOINT_PATH));
	}
}
