package com.kinlhp.moname.commons.jpa.it.ulid.uuid;

import java.util.UUID;

import jakarta.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidMonotonicUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl.SimpleUuidMonotonicUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.uuid.UuidMonotonicUlidIdentifiableRepository;

/**
 * Tests for UUID {@link Style#MONOTONIC MONOTONIC} styled ULID identifiable entities.
 */
class UuidMonotonicUlidGeneratorIT extends AbstractUuidUlidGeneratorIT<AbstractUuidMonotonicUlidIdentifiable> {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(UuidMonotonicUlidGeneratorIT.class);

	@Autowired
	@Nonnull
	private UuidMonotonicUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractUuidMonotonicUlidIdentifiable createEntity() {
		return SimpleUuidMonotonicUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return Style.MONOTONIC;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractUuidMonotonicUlidIdentifiable, UUID> getRepository() {
		return repository;
	}

	@Override
	public void warnBypassReason(@Nonnull final String profile) {
		super.warnBypassReason(profile);
		@Nonnull var message = "Isn't able to natively handle UUID type when executes the SQL query in a PreparedStatement directly, throwing:";
		if (MYSQL.equals(profile)) {
			message = """
					%s

					# MySQL:
					could not execute statement [Incorrect string value: '\\x95I\\x0D\\xA94K...' for column 'id' at row 1] [insert into uuid_ulid_identifiable (discriminator,id) values ('UUID_MONOTONIC',?)]
					Caused by: java.sql.SQLException: Incorrect string value: '\\x95I\\x0D\\xA94K...' for column 'id' at row 1"""
					.formatted(message);
		}
		LOG.warn(message);
	}
}
