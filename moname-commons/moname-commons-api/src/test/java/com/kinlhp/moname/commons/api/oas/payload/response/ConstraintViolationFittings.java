package com.kinlhp.moname.commons.api.oas.payload.response;

import com.kinlhp.moname.commons.api.oas.payload.GetEntitiesDefaultResponseDTO;

/**
 * Fittings for the bean tests.
 */
final class ConstraintViolationFittings {

	static GetEntitiesDefaultResponseDTO maxViolations() {
		return withoutViolations().code(600);
	}

	static GetEntitiesDefaultResponseDTO minViolations() {
		return withoutViolations().code(499);
	}

	static GetEntitiesDefaultResponseDTO notNullViolations() {
		return withoutViolations().code(null).description(null);
	}

	static GetEntitiesDefaultResponseDTO sizeViolations() {
		return withoutViolations().description("four");
	}

	static GetEntitiesDefaultResponseDTO withoutViolations() {
		return new GetEntitiesDefaultResponseDTO();
	}

}
