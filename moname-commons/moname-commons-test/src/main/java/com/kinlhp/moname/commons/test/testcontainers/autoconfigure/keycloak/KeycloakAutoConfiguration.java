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
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.testcontainers.containers.MySQLContainer;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;

import com.kinlhp.moname.commons.test.spring.security.autoconfigure.ssl.JwtDecoderSslBundleConfiguration;
import com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer;
import com.kinlhp.moname.commons.test.testcontainers.keycloak.WrappedKeycloakContainer;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.KEYCLOAK;

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

	@Nonnull
	static final String OPTIMIZED_PROFILE = "mysql & keycloak-optimized";

	@Nonnull
	static final String UNOPTIMIZED_PROFILE = "!(mysql & keycloak-optimized)";

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
	PropertiesKeycloakConnectionDetails keycloakConnectionDetails(
			@Nonnull final OAuth2ResourceServerProperties resourceServerProperties,
			@Nonnull final OAuth2ClientProperties clientProperties) {
		return new PropertiesKeycloakConnectionDetails(resourceServerProperties, clientProperties);
	}

	@Bean
	@ConditionalOnMissingBean(KeycloakContainerFactory.class)
	@Nonnull
	@Profile(UNOPTIMIZED_PROFILE)
	<T extends WrappedKeycloakContainer<T>> KeycloakContainerFactory<T> unoptimizedKeycloakContainerFactory(
			@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails) {
		return new UnoptimizedKeycloakContainerFactory<>(keycloakConnectionDetails);
	}

	@Bean
	@ConditionalOnMissingBean(KeycloakContainerFactory.class)
	@Nonnull
	@Profile(OPTIMIZED_PROFILE)
	<T extends ExtendedKeycloakContainer<T>> KeycloakContainerFactory<T> optimizedKeycloakContainerFactory(
			@Nonnull final PropertiesKeycloakConnectionDetails keycloakConnectionDetails,
			@Nonnull final ObjectProvider<MySQLContainer<?>> mysql) {
		return new OptimizedKeycloakContainerFactory<>(keycloakConnectionDetails, mysql);
	}

	@Bean({"dasniko.testcontainers.keycloak.ExtendableKeycloakContainer", KEYCLOAK})
	@ConditionalOnMissingBean(ExtendableKeycloakContainer.class)
	@Nonnull
	<T extends ExtendableKeycloakContainer<T>> T keycloakContainer(@Nonnull final KeycloakContainerFactory<T> factory) {
		LOG.debug("Obtaining Keycloak container from factory {}", factory.getClass().getSimpleName());
		return factory.getStartedSingleton();
	}
}
