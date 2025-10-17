package com.kinlhp.moname.commons.jpa.it.ulid;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable;
import com.kinlhp.moname.commons.test.spring.jpa.AbstractDataJpaTestSliceIT;

/**
 * Tests for ULID identifiable entities.
 */
@SuppressWarnings("java:S119")
public abstract class AbstractUlidGeneratorIT<T extends AbstractUlidIdentifiable<PK>, PK extends Serializable>
		extends AbstractDataJpaTestSliceIT implements UlidGeneratorIT<T, PK> {

	/**
	 * @deprecated Isn't able to natively handle type when executes the SQL query in a PreparedStatement directly.
	 */
	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	protected final String HSQLDB = "hsqldb";

	/**
	 * @deprecated Isn't able to natively handle type when executes the SQL query in a PreparedStatement directly.
	 */
	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	protected final String MYSQL = "mysql";

	/**
	 * @deprecated Isn't able to natively handle type when executes the SQL query in a PreparedStatement directly.
	 */
	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	protected final String ORACLE = "oracle";

	/**
	 * @deprecated Isn't able to natively handle type when executes the SQL query in a PreparedStatement directly.
	 */
	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	protected final String SQLSERVER = "sqlserver";

	@Autowired
	private DataSource dataSource;

	/**
	 * @deprecated Isn't able to natively handle type when executes the SQL query in a PreparedStatement directly.
	 */
	@Autowired
	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	private Environment environment;

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @deprecated Isn't able to natively handle type when executes the SQL query in a PreparedStatement directly.
	 */
	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	protected final Set<String> bypass(final String... profiles) {
		final Set<String> profilesToBypass = Set.of(profiles);
		final Set<String> bypassedProfiles = new HashSet<>();
		Arrays.stream(environment.getActiveProfiles())
				.filter(profilesToBypass::contains)
				.forEach(bypassedProfiles::add);
		return bypassedProfiles;
	}
}
