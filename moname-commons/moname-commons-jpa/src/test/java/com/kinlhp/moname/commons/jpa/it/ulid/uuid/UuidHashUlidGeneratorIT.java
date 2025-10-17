package com.kinlhp.moname.commons.jpa.it.ulid.uuid;

import java.util.UUID;

import jakarta.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidHashUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl.SimpleUuidHashUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.uuid.UuidHashUlidIdentifiableRepository;

/**
 * Tests for UUID {@link Style#HASH HASH} styled ULID identifiable entities.
 */
class UuidHashUlidGeneratorIT extends AbstractUuidUlidGeneratorIT<AbstractUuidHashUlidIdentifiable> {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(UuidHashUlidGeneratorIT.class);

	@Autowired
	@Nonnull
	private UuidHashUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractUuidHashUlidIdentifiable createEntity() {
		return SimpleUuidHashUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractUuidHashUlidIdentifiable, UUID> getRepository() {
		return repository;
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return Style.HASH;
	}

	@Override
	public void warnBypassReason(@Nonnull final String profile) {
		super.warnBypassReason(profile);
		@Nonnull var message = "Isn't able to natively handle UUID type when executes the SQL query in a PreparedStatement directly, throwing:";
		if (MYSQL.equals(profile)) {
			message = """
					%s

					# MySQL:
					could not execute statement [Incorrect string value: '\\x95I\\x0D\\xA9)p...' for column 'id' at row 1] [insert into uuid_ulid_identifiable (discriminator,id) values ('UUID_HASH',?)]
					Caused by: java.sql.SQLException: Incorrect string value: '\\x95I\\x0D\\xA9)p...' for column 'id' at row 1"""
					.formatted(message);
		}
		LOG.warn(message);
	}
}
