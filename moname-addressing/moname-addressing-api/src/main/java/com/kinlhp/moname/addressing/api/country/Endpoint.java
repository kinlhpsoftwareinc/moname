package com.kinlhp.moname.addressing.api.country;

import jakarta.annotation.Nonnull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;

import com.kinlhp.moname.addressing.api.oas.endpoint.CountriesApiDelegate;
import com.kinlhp.moname.addressing.api.oas.payload.CountriesResponse;
import com.kinlhp.moname.addressing.api.oas.payload.CountryResponse;
import com.kinlhp.moname.addressing.domain.country.Service;
import com.kinlhp.moname.commons.api.spring.support.RequestContextHolderSupport;

/**
 * <a href="https://stackoverflow.com/a/66296299">Read</a>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Endpoint extends RequestContextHolderSupport implements CountriesApiDelegate {

	private final Mapper mapper = Mapper.INSTANCE;
	private final Service service;

	@Override
	public ResponseEntity<CountriesResponse> getCountries(final Pageable pageable) {
		// noinspection removal
		requestReceived(); // TODO: Use Spring Boot Built-In Request Logging instead.
		final var page = service.findAll(pageable);
		final var response = mapper.map(page);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<CountryResponse> getCountry(final String numericCode) {
		// noinspection removal
		requestReceived(); // TODO: Use Spring Boot Built-In Request Logging instead.
		final var country = service.findByNumericCode(numericCode);
		final var response = mapper.map(country.orElseThrow(() -> notFound(numericCode)));
		return ResponseEntity.ok(response);
	}

	@Nonnull
	@Override
	public ErrorResponseException notFound(@Nonnull final String numericCode) {
		final var detail = String.format("Not found 'numeric-code' with value: '%s'", numericCode); // TODO: Localized message.
		LOG.warn(detail);
		return super.notFound(detail);
	}
}
