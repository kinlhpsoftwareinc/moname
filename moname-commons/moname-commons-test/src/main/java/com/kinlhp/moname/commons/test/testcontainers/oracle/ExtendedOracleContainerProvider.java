package com.kinlhp.moname.commons.test.testcontainers.oracle;

import org.jspecify.annotations.Nullable;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.jdbc.ConnectionUrl;
import org.testcontainers.oracle.OracleContainerProvider;
import org.testcontainers.utility.DockerImageName;

/**
 * Factory for extended Oracle containers.
 */
public class ExtendedOracleContainerProvider extends OracleContainerProvider {

	// TODO: - [Why Oracle Free Edition Will Sabotage Your Multi-Schema Application in Testcontainers(And What Actually Works)](https://medium.com/turkcell/why-oracle-free-edition-will-sabotage-your-multi-schema-application-in-testcontainers-and-what-e00f72f3f11c)
	//       - [Get the Linux-based container image](https://container-registry.oracle.com/ords/ocr/ba/database/free)
	private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("gvenzl/oracle-free");

	private static final String IMAGE;

	private static final String PASSWORD_PARAM = "password";

	private static final String USER_PARAM = "user";

	static {
		IMAGE = DEFAULT_IMAGE_NAME.getUnversionedPart();
	}

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
	public JdbcDatabaseContainer newInstance(final ConnectionUrl connectionUrl) {
		return newInstanceFromConnectionUrl(connectionUrl, USER_PARAM, PASSWORD_PARAM);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public JdbcDatabaseContainer newInstance(final @Nullable String tag) {
		final DockerImageName dockerImageName = DockerImageName.parse(IMAGE)
				.asCompatibleSubstituteFor(DockerImageName.parse("gvenzl/oracle-free"))
				.withTag(tag);
		return tag == null
				? newInstance()
				: new ExtendedOracleContainer(dockerImageName);
	}
}
