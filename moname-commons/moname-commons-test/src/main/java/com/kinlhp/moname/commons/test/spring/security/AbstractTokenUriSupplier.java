//package com.kinlhp.moname.commons.test.spring.security;
//
//import java.net.URI;
//
//import jakarta.annotation.Nonnull;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public abstract class AbstractTokenUriSupplier implements TokenUriSupplier {
//
//	@Nonnull
//	private static final Logger LOG = LoggerFactory.getLogger(AbstractTokenUriSupplier.class);
//
//	@Nonnull
//	private final URI tokenUri;
//
//	protected AbstractTokenUriSupplier(@Nonnull final URI tokenUri) {
//		LOG.debug("The token URI that will be supplied points to {}", tokenUri);
//		this.tokenUri = tokenUri;
//	}
//
//	@Override
//	public URI get() {
//		// TODO: A more concurrent/thread safe approach to be implemented
//		LOG.warn("Potential concurrency problem when parallel executions");
//		return tokenUri;
//	}
//}
