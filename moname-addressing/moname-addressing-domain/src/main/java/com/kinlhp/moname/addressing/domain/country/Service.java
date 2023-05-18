package com.kinlhp.moname.addressing.domain.country;

import java.util.Optional;

import jakarta.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class Service {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(Service.class);

	@Nonnull
	private final Repository repository;

	public Service(@Nonnull final Repository repository) {
		this.repository = repository;
	}

	/*
	https://blog.mnavarro.dev/the-repository-pattern-done-right
	https://github.com/mtarld/apip-ddd/issues/44
	https://programmingwithmosh.com/net/common-mistakes-with-the-repository-pattern/
	 */

	/**
	 * @param pageable the pageable to request a paged result.
	 */
	@Nonnull
	public Page<Entity> findAll(@Nonnull final Pageable pageable) {
		LOG.info("Finding all countries on page {} with size {}", pageable.getPageNumber(), pageable.getPageSize());
		final var slice = repository.stream().skip(pageable.getOffset()).limit(pageable.getPageSize());
		return new PageImpl<>(slice.toList());
	}

	/**
	 * @param numericCode three-digit (left padded with zero) country codes.
	 */
	@Nonnull
	public Optional<Entity> findByNumericCode(final int numericCode) {
		LOG.info("Finding country with numeric code {}", numericCode);
		return repository.one(Entity.numericCodeOf(numericCode));
	}
}
