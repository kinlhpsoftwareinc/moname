package com.kinlhp.moname.commons.test.testcontainers.oracle;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.jdbc.ConnectionUrl;
import org.testcontainers.oracle.OracleContainerProvider;
import org.testcontainers.utility.DockerImageName;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Factory for extended Oracle containers.
 */
public class ExtendedOracleContainerProvider extends OracleContainerProvider {

	// TODO: - [Why Oracle Free Edition Will Sabotage Your Multi-Schema Application in Testcontainers(And What Actually Works)](https://medium.com/turkcell/why-oracle-free-edition-will-sabotage-your-multi-schema-application-in-testcontainers-and-what-e00f72f3f11c)
	//       - [Get the Linux-based container image](https://container-registry.oracle.com/ords/ocr/ba/database/free)
	@Nonnull
	private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("gvenzl/oracle-free");

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
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance(@Nonnull final ConnectionUrl connectionUrl) {
		return newInstanceFromConnectionUrl(connectionUrl, USER_PARAM, PASSWORD_PARAM);
	}

	@Nonnull
	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance(@Nullable final String tag) {
		@Nonnull final DockerImageName dockerImageName = DockerImageName.parse(IMAGE)
				.asCompatibleSubstituteFor(DockerImageName.parse("gvenzl/oracle-free"))
				.withTag(tag);
		return tag == null
				? newInstance()
				: new ExtendedOracleContainer(dockerImageName);
	}
}
