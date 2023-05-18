package com.kinlhp.moname.commons.api.spring.autoconfigure;

import java.net.URI;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * <a href="https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#auto-configuration-files">
 * Auto-configuration Files
 * </a>
 */
@AutoConfiguration
@ControllerAdvice
public class ProblemDetailsExceptionHandlerAutoConfiguration extends ResponseEntityExceptionHandler {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(ProblemDetailsExceptionHandlerAutoConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Auto-configuring exception handler to return a response with RFC 7807 formatted error details in the body.");
	}

	@ExceptionHandler(value = {Exception.class})
	@Nonnull
	public final ProblemDetail handleConstraintViolationException(@Nonnull final Exception exception, @Nonnull final ServletWebRequest request) {
		@Nonnull final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
		problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
		problemDetail.setInstance(URI.create(request.getRequest().getRequestURI()));
		return problemDetail;
	}
}
