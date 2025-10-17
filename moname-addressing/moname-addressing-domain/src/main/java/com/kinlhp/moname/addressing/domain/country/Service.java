package com.kinlhp.moname.addressing.domain.country;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kinlhp.moname.addressing.repository.Countries;

public abstract class Service {

	private static final Logger LOG = LoggerFactory.getLogger(Service.class);

	private final Countries countries;

	protected Service(final Countries countries) {
		this.countries = countries;
	}

	protected Countries getCountries() {
		return countries;
	}

	/*
	https://blog.mnavarro.dev/the-repository-pattern-done-right
	https://github.com/mtarld/apip-ddd/issues/44
	https://programmingwithmosh.com/net/common-mistakes-with-the-repository-pattern/
	 */

	/**
	 * // TODO: Doc
	 */
	public Stream<Entity> getAll() {
		LOG.info("Getting all countries");
		return countries.all().stream();
	}

	public Stream<Entity> getSomeByFilter(final Predicate<Entity> predicate) {
		LOG.info("Getting countries that match the given predicate");
		return countries.filter(predicate).stream();
	}

	/**
	 * @see Service#getByNumericCode(String) getByNumericCode
	 */
	public Optional<Entity> getByNumericCode(final int numericCode) {
		return doGetByNumericCode(Entity.numericCodeOf(numericCode));
	}

	/**
	 * @param numericCode three-digit (left padded with zero) country codes.
	 */
	public Optional<Entity> getByNumericCode(final String numericCode) {
		return doGetByNumericCode(numericCode);
	}

	private Optional<Entity> doGetByNumericCode(final String numericCode) {
		LOG.info("Getting country with numeric code {}", numericCode);
		return countries.one(numericCode);
	}
}
