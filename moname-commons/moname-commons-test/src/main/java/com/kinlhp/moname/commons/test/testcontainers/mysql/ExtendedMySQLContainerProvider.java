package com.kinlhp.moname.commons.test.testcontainers.mysql;

import org.jspecify.annotations.Nullable;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.MySQLContainerProvider;
import org.testcontainers.utility.DockerImageName;

/**
 * Factory for extended MySQL containers.
 */
public class ExtendedMySQLContainerProvider extends MySQLContainerProvider {

	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance() {
		return newInstance(ExtendedMySQLContainer.DEFAULT_TAG);
	}

	@Override
	@SuppressWarnings({"deprecation", "rawtypes"})
	public JdbcDatabaseContainer newInstance(final @Nullable String tag) {
		final DockerImageName dockerImageName = DockerImageName.parse(MySQLContainer.IMAGE).withTag(tag);
		return tag == null
				? newInstance()
				: new ExtendedMySQLContainer(dockerImageName);
	}

	@Override
	public boolean supports(final String databaseType) {
		return databaseType.equals(ExtendedMySQLContainer.SUBPROTOCOL);
	}
}
