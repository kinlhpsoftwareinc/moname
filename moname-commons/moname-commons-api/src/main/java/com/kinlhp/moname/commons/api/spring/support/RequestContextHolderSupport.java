package com.kinlhp.moname.commons.api.spring.support;

import java.net.URI;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kinlhp.moname.commons.api.spring.exception.NotFoundException;
import com.kinlhp.moname.commons.api.spring.exception.NotImplementedException;

public abstract class RequestContextHolderSupport {

	private static final Logger LOG = LoggerFactory.getLogger(RequestContextHolderSupport.class);

	public HttpServletRequest currentRequest() {
		final var attributes = RequestContextHolder.currentRequestAttributes();
		return ((ServletRequestAttributes) attributes).getRequest();
	}

	public ErrorResponseException notFound(final String detail) {
		final var uri = URI.create(currentRequest().getRequestURI());
		return new NotFoundException(detail, uri);
	}

	public ErrorResponseException notImplemented(final String detail) {
		final var uri = URI.create(currentRequest().getRequestURI());
		return new NotImplementedException(detail, uri);
	}

	/**
	 * @see org.springframework.web.filter.CommonsRequestLoggingFilter CommonsRequestLoggingFilter
	 * @deprecated Use Spring Boot Built-In Request Logging instead.
	 */
	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	public void requestReceived() {
		LOG.info("Request received on {}", currentRequest().getRequestURI());
	}
}
