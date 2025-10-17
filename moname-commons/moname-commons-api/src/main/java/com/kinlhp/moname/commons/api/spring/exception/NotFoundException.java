package com.kinlhp.moname.commons.api.spring.exception;

import java.io.Serial;
import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class NotFoundException extends ErrorResponseException {

	@Serial
	private static final long serialVersionUID = -1406833860113193629L;

	private static final HttpStatus status = HttpStatus.NOT_FOUND;

	public NotFoundException(final String detail, final URI instance) {
		super(status, of(detail, instance), null);
	}

	private static ProblemDetail of(final String detail, final URI instance) {
		final var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setTitle(status.getReasonPhrase());
		problemDetail.setInstance(instance);
		return problemDetail;
	}
}
