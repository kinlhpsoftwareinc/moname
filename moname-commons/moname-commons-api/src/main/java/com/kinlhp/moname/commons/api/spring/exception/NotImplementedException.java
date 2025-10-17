package com.kinlhp.moname.commons.api.spring.exception;

import java.io.Serial;
import java.net.URI;

import jakarta.annotation.Nonnull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class NotImplementedException extends ErrorResponseException {

	@Serial
	private static final long serialVersionUID = -3672243825836204131L;

	@Nonnull
	private static final HttpStatus status = HttpStatus.NOT_IMPLEMENTED;

	public NotImplementedException(@Nonnull final String detail, @Nonnull final URI instance) {
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
