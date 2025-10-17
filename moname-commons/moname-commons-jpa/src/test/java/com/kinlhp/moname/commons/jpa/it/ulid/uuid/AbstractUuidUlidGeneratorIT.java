package com.kinlhp.moname.commons.jpa.it.ulid.uuid;

import java.util.Set;
import java.util.UUID;

import jakarta.annotation.Nonnull;

import com.github.f4b6a3.ulid.Ulid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.it.ulid.AbstractUlidGeneratorIT;

/**
 * Tests for UUID ULID identifiable entities.
 */
abstract class AbstractUuidUlidGeneratorIT<T extends AbstractUlidIdentifiable<UUID>>
		extends AbstractUlidGeneratorIT<T, UUID> {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(AbstractUuidUlidGeneratorIT.class);

	@Nonnull
	@Override
	public <R> R getUnwrappedSqlParameter(@Nonnull final UUID uuid) {
		LOG.debug("Unwrapping {} to {} SQL parameter", UUID.class.getName(), byte[].class.getName());
		return (R) Ulid.from(uuid).toBytes();
	}

	@Override
	public Set<String> supplyDirectSqlQueryBypassProfiles() {
		return bypass(MYSQL, ORACLE);
	}

	@Override
	public void warnBypassReason(@Nonnull final String profile) {
		@Nonnull var message = "Isn't able to natively handle ULID type when executes the SQL query in a PreparedStatement directly, throwing:";
		if (ORACLE.equals(profile)) {
			message = """
					%s

					# Oracle:
					java.sql.SQLException: ORA-17004: Invalid column type
					aused by: java.sql.SQLException: ORA-17004: Invalid column type"""
					.formatted(message);
		}
		LOG.warn(message);
	}
}
