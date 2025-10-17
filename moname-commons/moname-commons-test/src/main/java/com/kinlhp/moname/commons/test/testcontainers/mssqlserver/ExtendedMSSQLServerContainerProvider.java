package com.kinlhp.moname.commons.test.testcontainers.mssqlserver;

import org.jspecify.annotations.Nullable;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.MSSQLServerContainerProvider;
import org.testcontainers.jdbc.ConnectionUrl;
import org.testcontainers.utility.DockerImageName;

/**
 * Factory for extended MS SQL Server containers.
 */
public class ExtendedMSSQLServerContainerProvider extends MSSQLServerContainerProvider {

	private static final String PASSWORD_PARAM = "password";

	private static final String USER_PARAM = "user";

	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance() {
		return newInstance(ExtendedMSSQLServerContainer.DEFAULT_TAG);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance(final ConnectionUrl connectionUrl) {
		return super.newInstanceFromConnectionUrl(connectionUrl, USER_PARAM, PASSWORD_PARAM);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance(final @Nullable String tag) {
		final DockerImageName dockerImageName = DockerImageName.parse(MSSQLServerContainer.IMAGE).withTag(tag);
		return tag == null
				? newInstance()
				: new ExtendedMSSQLServerContainer(dockerImageName);
	}

	@Override
	public boolean supports(final String databaseType) {
		return databaseType.equals(ExtendedMSSQLServerContainer.SUBPROTOCOL);
	}
}
