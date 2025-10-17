package com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.LazyInitializationBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import jakarta.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Docker-free: registering a {@link LazyInitializationBeanFactoryPostProcessor} prevents the
 * {@code keycloakContainer} bean (which starts a real Testcontainer via
 * {@code factory.getStartedSingleton()}) from being eagerly instantiated merely by refreshing the
 * context. Setting {@code spring.main.lazy-initialization=true} as a plain property is NOT enough
 * here — that property is only interpreted by {@code SpringApplication}'s own bootstrap, not by the
 * bare {@link org.springframework.context.support.AbstractApplicationContext} that
 * {@link ApplicationContextRunner} refreshes, so the post-processor must be registered directly.
 * Only factory-bean presence and concrete type are asserted; factory construction itself never
 * touches Docker.
 */
@DisplayName("KeycloakAutoConfiguration")
class KeycloakAutoConfigurationTest {

	@Nonnull
	private static final ApplicationContextRunner CONTEXT_RUNNER = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(KeycloakAutoConfiguration.class))
			.withInitializer(context -> context.addBeanFactoryPostProcessor(new LazyInitializationBeanFactoryPostProcessor()));

	@Test
	@DisplayName("registers only the unoptimized factory when mysql & keycloak-optimized are inactive")
	void unoptimizedFactoryActiveWithoutMysqlProfile() {
		CONTEXT_RUNNER
				.withPropertyValues("spring.profiles.active=test,security")
				.run(context -> {
					assertThat(context).hasSingleBean(KeycloakContainerFactory.class);
					assertThat(context.getBean(KeycloakContainerFactory.class))
							.isInstanceOf(UnoptimizedKeycloakContainerFactory.class);
				});
	}

	@Test
	@DisplayName("registers only the unoptimized factory when mysql is active without keycloak-optimized")
	void unoptimizedFactoryActiveWithMysqlAloneProfile() {
		CONTEXT_RUNNER
				.withPropertyValues("spring.profiles.active=test,security,mysql")
				.run(context -> {
					assertThat(context).hasSingleBean(KeycloakContainerFactory.class);
					assertThat(context.getBean(KeycloakContainerFactory.class))
							.isInstanceOf(UnoptimizedKeycloakContainerFactory.class);
				});
	}

	@Test
	@DisplayName("registers only the optimized factory when mysql & keycloak-optimized are active")
	void optimizedFactoryActiveWithMysqlAndKeycloakOptimizedProfiles() {
		CONTEXT_RUNNER
				.withPropertyValues("spring.profiles.active=test,security,mysql,keycloak-optimized")
				.run(context -> {
					assertThat(context).hasSingleBean(KeycloakContainerFactory.class);
					assertThat(context.getBean(KeycloakContainerFactory.class))
							.isInstanceOf(OptimizedKeycloakContainerFactory.class);
				});
	}

	@Test
	@DisplayName("registers no factory or container bean without the security profile")
	void noBeansWithoutSecurityProfile() {
		CONTEXT_RUNNER
				.withPropertyValues("spring.profiles.active=test,mysql,keycloak-optimized")
				.run(context -> {
					assertThat(context).doesNotHaveBean(KeycloakContainerFactory.class);
					assertThat(context).doesNotHaveBean(KeycloakAutoConfiguration.class);
				});
	}

	@Test
	@DisplayName("registers no factory or container bean without the test profile")
	void noBeansWithoutTestProfile() {
		CONTEXT_RUNNER
				.withPropertyValues("spring.profiles.active=security,mysql,keycloak-optimized")
				.run(context -> {
					assertThat(context).doesNotHaveBean(KeycloakContainerFactory.class);
					assertThat(context).doesNotHaveBean(KeycloakAutoConfiguration.class);
				});
	}
}
