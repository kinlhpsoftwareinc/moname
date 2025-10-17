package com.kinlhp.moname.commons.jpa.it.ulid.ulid;

import java.util.Set;

import jakarta.annotation.Nonnull;

import com.github.f4b6a3.ulid.Ulid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.it.ulid.AbstractUlidGeneratorIT;

/**
 * Tests for ULID ULID identifiable entities.
 */
abstract class AbstractUlidUlidGeneratorIT<T extends AbstractUlidIdentifiable<Ulid>>
		extends AbstractUlidGeneratorIT<T, Ulid> {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(AbstractUlidUlidGeneratorIT.class);

	@Nonnull
	@Override
	public <R> R getUnwrappedSqlParameter(@Nonnull final Ulid ulid) {
		LOG.debug("Unwrapping {} to {} SQL parameter", Ulid.class.getName(), byte[].class.getName());
		return (R) ulid.toBytes();
	}

	@Override
	public Set<String> supplyDirectSqlQueryBypassProfiles() {
		return bypass(HSQLDB, ORACLE);
	}

	@Override
	public void warnBypassReason(@Nonnull final String profile) {
		@Nonnull var message = "Isn't able to natively handle ULID type when executes the SQL query in a PreparedStatement directly, throwing:";
		if (HSQLDB.equals(profile)) {
			message = """
					%s

					# HyperSQL (HSQLDB):
					java.sql.SQLSyntaxErrorException: incompatible data type in operation
					Caused by: org.hsqldb.HsqlException: incompatible data type in operation"""
					.formatted(message);
		} else if (ORACLE.equals(profile)) {
			message = """
					%s

					# Oracle:
					java.sql.SQLException: ORA-17004: Invalid column type
					Caused by: java.sql.SQLException: ORA-17004: Invalid column type"""
					.formatted(message);
		}
		LOG.warn(message);
	}
}
