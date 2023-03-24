package org.testcontainers.containers;

import org.testcontainers.jdbc.ConnectionUrl;
import org.testcontainers.utility.DockerImageName;

/**
 * Factory for extended MS SQL Server containers.
 */
public class ExtendedMSSQLServerContainerProvider extends MSSQLServerContainerProvider {
	private static final String USER_PARAM = "user";
	private static final String PASSWORD_PARAM = "password";

	@Override
	public boolean supports(final String databaseType) {
		return databaseType.equals(ExtendedMSSQLServerContainer.SUBPROTOCOL);
	}

	@Override
	@SuppressWarnings(value = {"rawtypes"})
	public JdbcDatabaseContainer newInstance() {
		return newInstance(ExtendedMSSQLServerContainer.DEFAULT_TAG);
	}

	@Override
	@SuppressWarnings(value = {"rawtypes"})
	public JdbcDatabaseContainer newInstance(final String tag) {
		final DockerImageName dockerImageName = DockerImageName
			.parse(ExtendedMSSQLServerContainer.IMAGE).withTag(tag);

		return tag == null
			? newInstance()
			: new ExtendedMSSQLServerContainer(dockerImageName);
	}

	@Override
	@SuppressWarnings(value = {"rawtypes", "resource"})
	public JdbcDatabaseContainer newInstance(final ConnectionUrl connectionUrl) {
		return newInstanceFromConnectionUrl(connectionUrl, USER_PARAM, PASSWORD_PARAM)
			.withDatabaseName(connectionUrl.getDatabaseName().orElseThrow().split(";")[0])
			.withUsername(ExtendedMSSQLServerContainer.MSSQL_USER)
			.withPassword(ExtendedMSSQLServerContainer.MSSQL_PASSWORD);
	}

}
