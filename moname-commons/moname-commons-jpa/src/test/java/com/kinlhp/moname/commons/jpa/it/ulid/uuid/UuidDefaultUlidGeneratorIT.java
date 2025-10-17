package com.kinlhp.moname.commons.jpa.it.ulid.uuid;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidDefaultUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.uuid.impl.SimpleUuidDefaultUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;
import com.kinlhp.moname.commons.jpa.repository.ulid.uuid.UuidDefaultUlidIdentifiableRepository;

/**
 * Tests for UUID default styled ULID identifiable entities.
 */
class UuidDefaultUlidGeneratorIT extends AbstractUuidUlidGeneratorIT<AbstractUuidDefaultUlidIdentifiable> {

	private static final Logger LOG = LoggerFactory.getLogger(UuidDefaultUlidGeneratorIT.class);

	@Autowired
	private UuidDefaultUlidIdentifiableRepository repository;

	@Override
	public AbstractUuidDefaultUlidIdentifiable createEntity() {
		return SimpleUuidDefaultUlidIdentifiable.builder().build();
	}

	@Override
	public Style getExpectedStyle() {
		return Style.AUTO;
	}

	@Override
	public UlidIdentifiableRepository<AbstractUuidDefaultUlidIdentifiable, UUID> getRepository() {
		return repository;
	}

	@Override
	public void warnBypassReason(final String profile) {
		super.warnBypassReason(profile);
		var message = "Isn't able to natively handle UUID type when executes the SQL query in a PreparedStatement directly, throwing:";
		if (MYSQL.equals(profile)) {
			message = """
					%s

					# MySQL:
					could not execute statement [Incorrect string value: '\\x95I\\x0D\\xA9\\x1D\\x06...' for column 'id' at row 1] [insert into uuid_ulid_identifiable (discriminator,id) values ('UUID_DEFAULT',?)]
					Caused by: java.sql.SQLException: Incorrect string value: '\\x95I\\x0D\\xA9\\x1D\\x06...' for column 'id' at row 1"""
					.formatted(message);
		}
		LOG.warn(message);
	}
}
