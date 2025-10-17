package com.kinlhp.moname.commons.jpa.it.ulid.uuid;

import java.util.UUID;

import jakarta.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidAutoUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl.SimpleUuidAutoUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.uuid.UuidAutoUlidIdentifiableRepository;

/**
 * Tests for UUID {@link Style#AUTO AUTO} styled ULID identifiable entities.
 */
class UuidAutoUlidGeneratorIT extends AbstractUuidUlidGeneratorIT<AbstractUuidAutoUlidIdentifiable> {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(UuidAutoUlidGeneratorIT.class);

	@Autowired
	@Nonnull
	private UuidAutoUlidIdentifiableRepository repository;

	@Nonnull
	@Override
	public AbstractUuidAutoUlidIdentifiable createEntity() {
		return SimpleUuidAutoUlidIdentifiable.builder().build();
	}

	@Nonnull
	@Override
	public Style getExpectedStyle() {
		return Style.AUTO;
	}

	@Nonnull
	@Override
	public UlidIdentifiableRepository<AbstractUuidAutoUlidIdentifiable, UUID> getRepository() {
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
					could not execute statement [Incorrect string value: '\\x95I\\x0D\\xA9\\x09\\xA7...' for column 'id' at row 1] [insert into uuid_ulid_identifiable (discriminator,id) values ('UUID_AUTO',?)]
					Caused by: java.sql.SQLException: Incorrect string value: '\\x95I\\x0D\\xA9\\x09\\xA7...' for column 'id' at row 1"""
					.formatted(message);
		}
		LOG.warn(message);
	}
}
