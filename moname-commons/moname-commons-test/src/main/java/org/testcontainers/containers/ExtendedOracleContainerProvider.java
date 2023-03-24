package org.testcontainers.containers;

import org.testcontainers.jdbc.ConnectionUrl;
import org.testcontainers.utility.DockerImageName;

/**
 * Factory for extended Oracle containers.
 */
public class ExtendedOracleContainerProvider extends OracleContainerProvider {

	private static final String USER_PARAM = "user";
	private static final String PASSWORD_PARAM = "password";

	@Override
	public boolean supports(final String databaseType) {
		return databaseType.equals(ExtendedOracleContainer.SUBPROTOCOL);
	}

	@Override
	@SuppressWarnings(value = {"rawtypes"})
	public JdbcDatabaseContainer newInstance() {
		return newInstance(ExtendedOracleContainer.DEFAULT_TAG);
	}

	@Override
	@SuppressWarnings(value = {"rawtypes"})
	public JdbcDatabaseContainer newInstance(final String tag) {
		final DockerImageName dockerImageName = DockerImageName
			.parse(ExtendedOracleContainer.IMAGE).withTag(tag);

		return tag == null
			? newInstance()
			: new ExtendedOracleContainer(dockerImageName);
	}

	@Override
	@SuppressWarnings(value = {"rawtypes", "resource"})
	public JdbcDatabaseContainer newInstance(final ConnectionUrl connectionUrl) {
		return newInstanceFromConnectionUrl(connectionUrl, USER_PARAM, PASSWORD_PARAM)
			.withDatabaseName(connectionUrl.getDatabaseName().orElseThrow());
	}

}
