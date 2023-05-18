package com.kinlhp.moname.commons.test.testcontainers;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MSSQLServerContainerProvider;
import org.testcontainers.jdbc.ConnectionUrl;
import org.testcontainers.utility.DockerImageName;

/**
 * Factory for extended MS SQL Server containers.
 */
//@NoArgsConstructor(onConstructor_ = {@Nonnull})
public class ExtendedMSSQLServerContainerProvider extends MSSQLServerContainerProvider {

	@Nonnull
	private static final String PASSWORD_PARAM = "password";
	@Nonnull
	private static final String USER_PARAM = "user";

	@Nonnull
	@Override
	@SuppressWarnings(value = "rawtypes")
	public JdbcDatabaseContainer newInstance() {
		return newInstance(ExtendedMSSQLServerContainer.DEFAULT_TAG);
	}

	@Nonnull
	@Override
	@SuppressWarnings(value = {"rawtypes", "resource"})
	public JdbcDatabaseContainer newInstance(@Nonnull final ConnectionUrl connectionUrl) {
		return newInstanceFromConnectionUrl(connectionUrl, USER_PARAM, PASSWORD_PARAM)
			.withDatabaseName(connectionUrl.getDatabaseName().orElseThrow().split(";")[0])
			.withPassword(ExtendedMSSQLServerContainer.MSSQL_PASSWORD)
			.withUsername(ExtendedMSSQLServerContainer.MSSQL_USER);
	}

	@Nonnull
	@Override
	@SuppressWarnings(value = "rawtypes")
	public JdbcDatabaseContainer newInstance(@Nullable final String tag) {
		@Nonnull final DockerImageName dockerImageName = DockerImageName.parse(ExtendedMSSQLServerContainer.IMAGE).withTag(tag);
		return tag == null
			? newInstance()
			: new ExtendedMSSQLServerContainer(dockerImageName);
	}

	@Override
	public boolean supports(@Nonnull final String databaseType) {
		return databaseType.equals(ExtendedMSSQLServerContainer.SUBPROTOCOL);
	}
}
