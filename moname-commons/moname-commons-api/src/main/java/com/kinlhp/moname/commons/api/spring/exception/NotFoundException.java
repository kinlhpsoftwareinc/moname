package com.kinlhp.moname.commons.api.spring.exception;

import java.io.Serial;
import java.net.URI;

import jakarta.annotation.Nonnull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class NotFoundException extends ErrorResponseException {

	@Serial
	private static final long serialVersionUID = -1406833860113193629L;

	@Nonnull
	private static final HttpStatus status = HttpStatus.NOT_FOUND;

	public NotFoundException(@Nonnull final String detail, @Nonnull final URI instance) {
		super(status, of(detail, instance), null);
	}

	@Nonnull
	private static ProblemDetail of(@Nonnull final String detail, @Nonnull final URI instance) {
		@Nonnull final var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setTitle(status.getReasonPhrase());
		problemDetail.setInstance(instance);
		return problemDetail;
	}
}
