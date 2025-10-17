package com.kinlhp.moname.addressing.api.country;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;

import com.kinlhp.moname.addressing.api.oas.endpoint.CountriesApiDelegate;
import com.kinlhp.moname.addressing.api.oas.payload.Country;
import com.kinlhp.moname.commons.api.spring.support.RequestContextHolderSupport;

/**
 * <a href="https://stackoverflow.com/a/66296299">
 * Significance of Delegate Design Pattern in Swagger Generated Code?
 * </a>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Endpoint extends RequestContextHolderSupport implements CountriesApiDelegate {

	private final Mapper mapper = Mapper.INSTANCE;

	private final Service service;

	@Override
	public ResponseEntity<Set<Country>> getCountries(final Pageable pageable) {
		//noinspection removal
		requestReceived(); // TODO: Use Spring Boot Built-In Request Logging instead.
		final var page = service.getAll(pageable);
		final var response = mapper.map(page);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Country> getCountry(final String numericCode) throws Exception {
		//noinspection removal
		requestReceived(); // TODO: Use Spring Boot Built-In Request Logging instead.
		final var country = service.getByNumericCode(numericCode);
		final var response = mapper.map(country.orElseThrow(() -> notFound(numericCode)));
		return ResponseEntity.ok(response);
	}

	@Override
	public ErrorResponseException notFound(final String numericCode) {
		final var detail = "Not found 'numeric-code' with value: '%s'".formatted(numericCode); // TODO: Localized message.
		LOG.warn(detail);
		return super.notFound(detail);
	}
}
