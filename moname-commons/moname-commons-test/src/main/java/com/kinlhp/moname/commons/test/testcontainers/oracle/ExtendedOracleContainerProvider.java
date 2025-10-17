package com.kinlhp.moname.commons.test.testcontainers.oracle;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.OracleContainerProvider;
import org.testcontainers.jdbc.ConnectionUrl;
import org.testcontainers.utility.DockerImageName;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Factory for extended Oracle containers.
 */
public class ExtendedOracleContainerProvider extends OracleContainerProvider
		/*// TODO: implements ConfigurableMemory<ExtendedOracleContainer>*/ {

	@Nonnull
	private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("gvenzl/oracle-xe");

	@Nonnull
	private static final String IMAGE;

	@Nonnull
	private static final String PASSWORD_PARAM = "password";

	@Nonnull
	private static final String USER_PARAM = "user";

	static {
		IMAGE = DEFAULT_IMAGE_NAME.getUnversionedPart();
	}

	@Override
	public boolean supports(@Nonnull final String databaseType) {
		return databaseType.equals(ExtendedOracleContainer.SUBPROTOCOL);
	}

	@Nonnull
	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance() {
		return newInstance(ExtendedOracleContainer.DEFAULT_TAG);
	}

	@Nonnull
	@Override
	@SuppressWarnings({"rawtypes", "resource"})
	public JdbcDatabaseContainer newInstance(@Nonnull final ConnectionUrl connectionUrl) {
		return newInstanceFromConnectionUrl(connectionUrl, USER_PARAM, PASSWORD_PARAM)
				//.withDatabaseName(connectionUrl.getDatabaseName().orElseThrow())
				;
	}

	@Nonnull
	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance(@Nullable final String tag) {
		@Nonnull final DockerImageName dockerImageName = DockerImageName.parse(IMAGE).withTag(tag);
		return tag == null
				? newInstance()
				: new ExtendedOracleContainer(dockerImageName);
	}
}
