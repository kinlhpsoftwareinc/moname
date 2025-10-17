package com.kinlhp.moname.addressing.api.country;

import java.util.Set;

import jakarta.annotation.Nonnull;

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

	@Nonnull
	private final Mapper mapper = Mapper.INSTANCE;

	@Nonnull
	private final Service service;

	@Nonnull
	@Override
	public ResponseEntity<Set<Country>> getCountries(@Nonnull final Pageable pageable) {
		//noinspection removal
		requestReceived(); // TODO: Use Spring Boot Built-In Request Logging instead.
		@Nonnull final var page = service.getAll(pageable);
		@Nonnull final var response = mapper.map(page);
		return ResponseEntity.ok(response);
	}

	@Nonnull
	@Override
	public ResponseEntity<Country> getCountry(@Nonnull final String numericCode) throws Exception {
		//noinspection removal
		requestReceived(); // TODO: Use Spring Boot Built-In Request Logging instead.
		@Nonnull final var country = service.getByNumericCode(numericCode);
		@Nonnull final var response = mapper.map(country.orElseThrow(() -> notFound(numericCode)));
		return ResponseEntity.ok(response);
	}

	@Nonnull
	@Override
	public ErrorResponseException notFound(@Nonnull final String numericCode) {
		@Nonnull final var detail = "Not found 'numeric-code' with value: '%s'".formatted(numericCode); // TODO: Localized message.
		LOG.warn(detail);
		return super.notFound(detail);
	}
}
