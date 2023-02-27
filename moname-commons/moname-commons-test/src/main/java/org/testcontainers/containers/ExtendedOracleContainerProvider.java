package org.testcontainers.containers;

import org.testcontainers.utility.DockerImageName;

/**
 * Factory for extended Oracle containers.
 */
public class ExtendedOracleContainerProvider extends OracleContainerProvider {

	@Override
	public boolean supports(final String databaseType) {
		return databaseType.equals(ExtendedOracleContainer.SUBPROTOCOL);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance() {
		return newInstance(ExtendedOracleContainer.DEFAULT_TAG);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance(final String tag) {
		final DockerImageName dockerImageName = DockerImageName
			.parse(ExtendedOracleContainer.IMAGE).withTag(tag);

		return tag == null
			? newInstance()
			: new ExtendedOracleContainer(dockerImageName);
	}

}
