package org.testcontainers.containers;

import org.testcontainers.utility.DockerImageName;

/**
 * Factory for extended MySQL containers.
 */
public class ExtendedMySQLContainerProvider extends MySQLContainerProvider {

	@Override
	public boolean supports(final String databaseType) {
		return databaseType.equals(ExtendedMySQLContainer.SUBPROTOCOL);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance() {
		return newInstance(ExtendedMySQLContainer.DEFAULT_TAG);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance(final String tag) {
		final DockerImageName dockerImageName = DockerImageName
			.parse(ExtendedMySQLContainer.IMAGE).withTag(tag);

		return tag == null
			? newInstance()
			: new ExtendedMySQLContainer(dockerImageName);
	}

}
