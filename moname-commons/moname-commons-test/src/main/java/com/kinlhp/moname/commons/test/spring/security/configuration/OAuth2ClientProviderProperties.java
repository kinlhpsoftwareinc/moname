// package com.kinlhp.moname.commons.test.spring.security.configuration;
//
// import org.springframework.boot.context.properties.ConfigurationProperties;
//
// import jakarta.annotation.Nonnull;
// import java.net.URI;
// import java.util.function.Supplier;
//
// import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.JWKS_PATH;
// import static com.kinlhp.moname.commons.test.testcontainers.keycloak.ExtendedKeycloakContainer.TOKEN_ENDPOINT_PATH;
//
// @ConfigurationProperties(value = "spring.security.oauth2.client.provider")
// public class OAuth2ClientProviderProperties {
//
// 	private Keycloak keycloak;
//
// 	@Nonnull
// 	public Keycloak keycloak() {
// 		return keycloak;
// 	}
//
// 	public void setKeycloak(@Nonnull final Keycloak keycloak) {
// 		this.keycloak = keycloak;
// 	}
//
// 	@Nonnull
// 	public URI issuerUri() {
// 		return keycloak().issuerUri();
// 	}
//
// 	public void setIssuerUri(@Nonnull final URI issuerUri) {
// 		keycloak().setIssuerUri(issuerUri);
// 	}
//
// 	@Nonnull
// 	public URI jwksUri() {
// 		return keycloak().jwksUri();
// 	}
//
// 	@Nonnull
// 	public URI tokenUri() {
// 		return keycloak().tokenUri();
// 	}
//
// 	static class Keycloak {
//
// 		@Nonnull
// 		private Supplier<URI> issuerUriSupplier = () -> URI.create("about:blank");
//
// 		@Nonnull
// 		URI issuerUri() {
// 			return issuerUriSupplier.get();
// 		}
//
// 		void setIssuerUri(@Nonnull final URI issuerUri) {
// 			issuerUriSupplier = () -> issuerUri;
// 		}
//
// 		@Nonnull
// 		URI jwksUri() {
// 			return URI.create("%s%s".formatted(issuerUri(), JWKS_PATH));
// 		}
//
// 		@Nonnull
// 		URI tokenUri() {
// 			return URI.create("%s%s".formatted(issuerUri(), TOKEN_ENDPOINT_PATH));
// 		}
//
// 		void setTokenUri(@Nonnull final URI tokenUri) {
// 			@Nonnull final var issuerUri = tokenUri.toString().replace(TOKEN_ENDPOINT_PATH, "");
// 			setIssuerUri(URI.create(issuerUri));
// 		}
// 	}
// }
