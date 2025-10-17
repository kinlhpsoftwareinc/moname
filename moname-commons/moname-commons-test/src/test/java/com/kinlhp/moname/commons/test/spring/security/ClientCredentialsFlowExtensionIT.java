package com.kinlhp.moname.commons.test.spring.security;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.ssl.SslAutoConfiguration;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;

import com.kinlhp.moname.commons.test.spring.context.IfSecurityProfile;
import com.kinlhp.moname.commons.test.spring.jpa.autoconfigure.persistenceunit.PersistenceUnitNameCustomizerAutoConfiguration;
import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.KeycloakAutoConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.keycloak.admin.client.resource.BearerAuthFilter.AUTH_HEADER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import static com.kinlhp.moname.commons.test.spring.security.MockMvcHeaderCapturer.headerOf;

/**
 * <strong>NOTE:</strong> Data JPA is autoconfigured alongside the web slice because the optimized Keycloak container
 * requires the MySQL container (hostname {@literal mysql} on the shared network), which is only started when the
 * entity manager factory opens its first JDBC connection.
 */
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DisplayName("@ClientCredentialsFlow against Keycloak")
@IfSecurityProfile
@ImportAutoConfiguration({KeycloakAutoConfiguration.class, MockMvcAutoConfiguration.class,
		PersistenceUnitNameCustomizerAutoConfiguration.class, SslAutoConfiguration.class})
@WebMvcTest
class ClientCredentialsFlowExtensionIT {

	@Nonnull
	private static final String NON_EXISTENT_SECRET = "non-existent";

	@DisplayName("when a constructor parameter is annotated")
	@Nested
	class ConstructorTarget {

		@Nonnull
		private final MockMvc resolvedMockMvc;

		ConstructorTarget(@ClientCredentialsFlow @Nonnull final MockMvc resolvedMockMvc) {
			this.resolvedMockMvc = resolvedMockMvc;
		}

		@DisplayName("resolves a MockMvc carrying the bearer token of the client")
		@Test
		final void resolvesMockMvcCarryingBearerToken() {
			assertThat(headerOf(resolvedMockMvc, AUTHORIZATION))
					.startsWith(AUTH_HEADER_PREFIX)
					.hasSizeGreaterThan(AUTH_HEADER_PREFIX.length());
		}

		@DisplayName("and two parameters have equal annotations, implicit or explicit")
		@Nested
		class EqualAnnotations {

			@Nonnull
			private final MockMvc implicitClientCredentialsResolvedMockMvc;

			@Nonnull
			private final MockMvc explicitClientCredentialsResolvedMockMvc;

			EqualAnnotations(
					@ClientCredentialsFlow
					@Nonnull
					final MockMvc implicitClientCredentialsResolvedMockMvc,
					@ClientCredentialsFlow(
							clientId = "moname-commons-api",
							clientSecret = "moname",
							grantType = "client_credentials"
					)
					@Nonnull
					final MockMvc explicitClientCredentialsResolvedMockMvc) {
				this.implicitClientCredentialsResolvedMockMvc = implicitClientCredentialsResolvedMockMvc;
				this.explicitClientCredentialsResolvedMockMvc = explicitClientCredentialsResolvedMockMvc;
			}

			@DisplayName("resolves both with the same cached token")
			@Test
			final void resolvesBothWithSameCachedToken() {
				@Nullable final var implicitClientCredentialsAuthorizationHeader =
						headerOf(implicitClientCredentialsResolvedMockMvc, AUTHORIZATION);
				assertThat(implicitClientCredentialsAuthorizationHeader).isNotNull().startsWith(AUTH_HEADER_PREFIX);
				// Two requests to Keycloak would yield different tokens (distinct jti), so equality proves the cache hit
				assertThat(headerOf(explicitClientCredentialsResolvedMockMvc, AUTHORIZATION))
						.isEqualTo(implicitClientCredentialsAuthorizationHeader);
			}
		}

		@DisplayName("and two parameters have distinct annotations")
		@Nested
		class DistinctAnnotations {

			@Nonnull
			private final MockMvc validClientCredentialsResolvedMockMvc;

			@Nonnull
			private final MockMvc rejectedClientCredentialsResolvedMockMvc;

			DistinctAnnotations(
					@ClientCredentialsFlow
					@Nonnull
					final MockMvc validClientCredentialsResolvedMockMvc,
					@ClientCredentialsFlow(
							clientId = "constructor.non-existent@moname",
							clientSecret = NON_EXISTENT_SECRET
					)
					@Nonnull
					final MockMvc rejectedClientCredentialsResolvedMockMvc) {
				this.validClientCredentialsResolvedMockMvc = validClientCredentialsResolvedMockMvc;
				this.rejectedClientCredentialsResolvedMockMvc = rejectedClientCredentialsResolvedMockMvc;
			}

			@DisplayName("resolves each with the token of its own client")
			@Test
			final void resolvesEachWithTokenOfItsOwnClient() {
				assertThat(headerOf(validClientCredentialsResolvedMockMvc, AUTHORIZATION))
						.startsWith(AUTH_HEADER_PREFIX);
				assertThat(headerOf(rejectedClientCredentialsResolvedMockMvc, AUTHORIZATION)).isNull();
			}
		}
	}

	@DisplayName("when the test method is annotated")
	@Nested
	class MethodTarget {

		@Nonnull
		private final MockMvc injectedMockMvc;

		MethodTarget(@Autowired @Nonnull final MockMvc injectedMockMvc) {
			this.injectedMockMvc = injectedMockMvc;
		}

		@ClientCredentialsFlow
		@DisplayName("resolves a MockMvc carrying the bearer token of the client")
		@Test
		final void resolvesMockMvcCarryingBearerToken(@Nonnull final MockMvc mockMvc) {
			assertThat(headerOf(mockMvc, AUTHORIZATION))
					.startsWith(AUTH_HEADER_PREFIX)
					.hasSizeGreaterThan(AUTH_HEADER_PREFIX.length());
		}

		@ClientCredentialsFlow(clientId = "method.non-existent@moname", clientSecret = NON_EXISTENT_SECRET)
		@DisplayName("resolves a MockMvc carrying an empty token when Keycloak rejects the client")
		@Test
		final void resolvesMockMvcCarryingEmptyTokenForRejectedClient(@Nonnull final MockMvc mockMvc) {
			assertThat(headerOf(mockMvc, AUTHORIZATION)).isNull();
		}

		@ClientCredentialsFlow
		@DisplayName("leaves an @Autowired MockMvc parameter to Spring, without the Authorization header")
		@Test
		final void leavesAutowiredParameterUntouched(@Autowired @Nonnull final MockMvc mockMvc) {
			assertThat(headerOf(mockMvc, AUTHORIZATION)).isNull();
		}

		@ClientCredentialsFlow
		@DisplayName("leaves an injected MockMvc field untouched after resolving an authorized one")
		@Test
		final void leavesInjectedFieldUntouched(@Nonnull final MockMvc mockMvc) {
			assertThat(headerOf(mockMvc, AUTHORIZATION)).startsWith(AUTH_HEADER_PREFIX);
			assertThat(headerOf(injectedMockMvc, AUTHORIZATION)).isNull();
		}
	}

	@DisplayName("when the method parameter is annotated")
	@Nested
	class ParameterTarget {

		@DisplayName("resolves a MockMvc carrying the bearer token of the client")
		@Test
		final void resolvesMockMvcCarryingBearerToken(@ClientCredentialsFlow @Nonnull final MockMvc resolvedMockMvc) {
			assertThat(headerOf(resolvedMockMvc, AUTHORIZATION))
					.startsWith(AUTH_HEADER_PREFIX)
					.hasSizeGreaterThan(AUTH_HEADER_PREFIX.length());
		}

		@DisplayName("resolves a MockMvc carrying an empty token when Keycloak rejects the client")
		@Test
		final void resolvesMockMvcCarryingEmptyTokenForRejectedClient(
				@ClientCredentialsFlow(
						clientId = "parameter.non-existent@moname",
						clientSecret = NON_EXISTENT_SECRET
				)
				@Nonnull final MockMvc resolvedMockMvc
		) {
			assertThat(headerOf(resolvedMockMvc, AUTHORIZATION)).isNull();
		}
	}

	@DisplayName("when a lifecycle method declares the MockMvc parameter")
	@Nested
	class LifecycleMethod {

		@DisplayName("and only the test method is annotated")
		@Nested
		class AnnotatedTestMethod {

			@Nullable
			private MockMvc resolvedMockMvc = null;

			@BeforeEach
			final void resolve(@Nonnull final MockMvc resolvedMockMvc) {
				this.resolvedMockMvc = resolvedMockMvc;
			}

			@ClientCredentialsFlow
			@DisplayName("resolves the unannotated parameter with the client of the test method")
			@Test
			final void resolvesUnannotatedParameterWithClientOfTestMethod() {
				assertThat(headerOf(resolvedMockMvc, AUTHORIZATION)).startsWith(AUTH_HEADER_PREFIX);
			}
		}

		@DisplayName("and the parameter is annotated")
		@Nested
		class AnnotatedParameter {

			@Nullable
			private MockMvc resolvedMockMvc = null;

			@BeforeEach
			final void resolve(@ClientCredentialsFlow @Nonnull final MockMvc resolvedMockMvc) {
				this.resolvedMockMvc = resolvedMockMvc;
			}

			@DisplayName("resolves the parameter with its own client")
			@Test
			final void resolvesAnnotatedParameter() {
				assertThat(headerOf(resolvedMockMvc, AUTHORIZATION)).startsWith(AUTH_HEADER_PREFIX);
			}
		}
	}

	@DisplayName("when used as a meta-annotation")
	@Nested
	class MetaAnnotation {

		@DefaultClientCredentials
		@DisplayName("resolves through a composed annotation on the test method")
		@Test
		final void resolvesThroughComposedAnnotationOnMethod(@Nonnull final MockMvc resolvedMockMvc) {
			assertThat(headerOf(resolvedMockMvc, AUTHORIZATION)).startsWith(AUTH_HEADER_PREFIX);
		}

		@DisplayName("resolves through a composed annotation on the parameter")
		@Test
		final void resolvesThroughComposedAnnotationOnParameter(
				@DefaultClientCredentials @Nonnull final MockMvc resolvedMockMvc) {
			assertThat(headerOf(resolvedMockMvc, AUTHORIZATION)).startsWith(AUTH_HEADER_PREFIX);
		}
	}

	@DisplayName("caching tokens per annotation attributes")
	@Nested
	class Cache {

		@DisplayName("shares the token between equal annotations, implicit or explicit")
		@Test
		final void sharesTokenBetweenEqualAnnotations(
				@ClientCredentialsFlow
				@Nonnull final MockMvc implicitClientCredentialsResolvedMockMvc,
				@ClientCredentialsFlow(
						clientId = "moname-commons-api",
						clientSecret = "moname",
						grantType = "client_credentials"
				)
				@Nonnull final MockMvc explicitClientCredentialsResolvedMockMvc) {
			@Nullable final var implicitClientCredentialsAuthorizationHeader =
					headerOf(implicitClientCredentialsResolvedMockMvc, AUTHORIZATION);
			assertThat(implicitClientCredentialsAuthorizationHeader).isNotNull().startsWith(AUTH_HEADER_PREFIX);
			// Two requests to Keycloak would yield different tokens (distinct jti), so equality proves the cache hit
			assertThat(headerOf(explicitClientCredentialsResolvedMockMvc, AUTHORIZATION))
					.isEqualTo(implicitClientCredentialsAuthorizationHeader);
		}

		@DisplayName("keeps a separate entry per distinct annotation")
		@Test
		final void keepsSeparateEntriesForDistinctAnnotations(
				@ClientCredentialsFlow
				@Nonnull final
				MockMvc validClientCredentialsResolvedMockMvc,
				@ClientCredentialsFlow(
						clientId = "cache.non-existent@moname",
						clientSecret = NON_EXISTENT_SECRET
				)
				@Nonnull final
				MockMvc rejectedCredentialsResolvedMockMvc) {
			assertThat(headerOf(validClientCredentialsResolvedMockMvc, AUTHORIZATION)).startsWith(AUTH_HEADER_PREFIX);
			assertThat(headerOf(rejectedCredentialsResolvedMockMvc, AUTHORIZATION)).isNull();
		}

		@DisplayName("when the cached token has expired")
		@Nested
		@TestInstance(Lifecycle.PER_CLASS)
		class ExpiredEntry {

			@Nullable
			private ShortLivedKeycloakClientFixture shortLivedClient = null;

			@Nullable
			private String expiredAuthorizationHeader = null;

			@BeforeAll
			final void registerShortLivedClient(@Autowired @Nonnull final ExtendableKeycloakContainer<?> keycloak,
					@Autowired @Nonnull final SslBundles sslBundles) {
				shortLivedClient = ShortLivedKeycloakClientFixture.register(keycloak, sslBundles);
			}

			@AfterAll
			final void removeShortLivedClient() {
				if (shortLivedClient != null) {
					shortLivedClient.close();
				}
			}

			@BeforeEach
			@SuppressWarnings("java:S2925")
			final void cacheTokenAndLetItExpire(
					@ClientCredentialsFlow(clientId = ShortLivedKeycloakClientFixture.CLIENT_ID)
					@Nonnull
					final MockMvc firstResolvedMockMvc
			) throws InterruptedException {
				expiredAuthorizationHeader = headerOf(firstResolvedMockMvc, AUTHORIZATION);
				assertThat(expiredAuthorizationHeader).isNotNull().startsWith(AUTH_HEADER_PREFIX);
				Thread.sleep(Duration.ofSeconds(2L));
			}

			@DisplayName("requests a new one")
			@Test
			final void renewsExpiredToken(
					@ClientCredentialsFlow(clientId = ShortLivedKeycloakClientFixture.CLIENT_ID)
					@Nonnull
					final MockMvc renewedMockMvc
			) {
				assertThat(headerOf(renewedMockMvc, AUTHORIZATION))
						.startsWith(AUTH_HEADER_PREFIX)
						.isNotEqualTo(expiredAuthorizationHeader);
			}
		}
	}

	@ClientCredentialsFlow
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.PARAMETER})
	@interface DefaultClientCredentials {
	}
}
