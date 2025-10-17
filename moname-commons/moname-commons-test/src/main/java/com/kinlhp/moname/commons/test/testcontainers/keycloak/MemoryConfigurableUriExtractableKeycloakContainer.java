//package com.kinlhp.moname.commons.test.testcontainers.keycloak;
//
//import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
//import org.jetbrains.annotations.NotNull;
//import org.testcontainers.containers.GenericContainer;
//
//import jakarta.annotation.Nonnull;
//import java.net.URI;
//import java.util.Optional;
//
//import com.kinlhp.moname.commons.test.testcontainers.MemoryConfigurableUriExtractableContainer;
//
//// TODO: Tests to be implemented
//@SuppressWarnings("java:S119")
//public interface MemoryConfigurableUriExtractableKeycloakContainer<SELF extends ExtendableKeycloakContainer<SELF>
//		& MemoryConfigurableUriExtractableContainer<SELF>>
//		extends MemoryConfigurableUriExtractableContainer<SELF> {
//
//	@Nonnull
//	Optional<URI> getIssuerUri();
//
//	@Nonnull
//	SELF withIssuerUri(@Nonnull URI issuerUri);
//
//	@Nonnull
//	@Override
//	@SuppressWarnings({"TypeParameterHidesVisibleType", "java:S4977"})
//	default <SELF extends GenericContainer<SELF>> SELF getContainer() {
//		//noinspection unchecked
//		return (SELF) this;
//	}
//
//	@Override
//	@NotNull
//	default Optional<URI> getUri() {
//		return getIssuerUri();
//	}
//}
