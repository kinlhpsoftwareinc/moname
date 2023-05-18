package com.kinlhp.moname.commons.api.spring.support;

import java.net.URI;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kinlhp.moname.commons.api.spring.exception.NotFoundException;
import com.kinlhp.moname.commons.api.spring.exception.NotImplementedException;

public abstract class RequestContextHolderSupport {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(RequestContextHolderSupport.class);

	@Nonnull
	public HttpServletRequest currentRequest() {
		@Nonnull final var attributes = RequestContextHolder.currentRequestAttributes();
		return ((ServletRequestAttributes) attributes).getRequest();
	}

	@Nonnull
	public ErrorResponseException notFound(@Nonnull final String detail) {
		@Nonnull final var uri = URI.create(currentRequest().getRequestURI());
		return new NotFoundException(detail, uri);
	}

	@Nonnull
	public ErrorResponseException notImplemented(@Nonnull final String detail) {
		@Nonnull final var uri = URI.create(currentRequest().getRequestURI());
		return new NotImplementedException(detail, uri);
	}

	/**
	 * @see org.springframework.web.filter.CommonsRequestLoggingFilter
	 * @deprecated Use Spring Boot Built-In Request Logging instead.
	 */
	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	public void requestReceived() {
		LOG.info("Request received on {}", currentRequest().getRequestURI());
	}
}
