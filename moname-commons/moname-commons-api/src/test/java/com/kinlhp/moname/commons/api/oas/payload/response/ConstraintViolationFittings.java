package com.kinlhp.moname.commons.api.oas.payload.response;

import java.net.URI;

import org.springframework.http.HttpStatus;

import com.kinlhp.moname.commons.api.oas.payload.ProblemDetailResponse;

/**
 * Fittings for the bean tests.
 */
final class ConstraintViolationFittings {

	static ProblemDetailResponse maxViolations() {
		return withoutViolations().status(512);
	}

	static ProblemDetailResponse minViolations() {
		return withoutViolations().status(399);
	}

	static ProblemDetailResponse notNullViolations() {
		return new ProblemDetailResponse(null, null, null, null, null);
	}

	static ProblemDetailResponse sizeViolations() {
		return withoutViolations()
			.detail("")
			.title("");
	}

	static ProblemDetailResponse withoutViolations() {
		return new ProblemDetailResponse(
			"Failed to convert 'bar' with value: 'foo'",
			URI.create("/v1/bars/foo"),
			HttpStatus.BAD_REQUEST.value(),
			"Bad Request",
			URI.create("about:blank")
		);
	}

}
