package org.testcontainers.containers;

import org.testcontainers.utility.DockerImageName;

/**
 * Factory for extended MS SQL Server containers.
 */
public class ExtendedMSSQLServerContainerProvider extends MSSQLServerContainerProvider {

	@Override
	public boolean supports(final String databaseType) {
		return databaseType.equals(ExtendedMSSQLServerContainer.SUBPROTOCOL);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance() {
		return newInstance(ExtendedMSSQLServerContainer.DEFAULT_TAG);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance(final String tag) {
		final DockerImageName dockerImageName = DockerImageName
			.parse(ExtendedMSSQLServerContainer.IMAGE).withTag(tag);

		return tag == null
			? newInstance()
			: new ExtendedMSSQLServerContainer(dockerImageName);
	}

}
