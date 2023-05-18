package com.kinlhp.moname.commons.test.testcontainers;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainerProvider;
import org.testcontainers.utility.DockerImageName;

/**
 * Factory for extended MySQL containers.
 */
//@NoArgsConstructor(onConstructor_ = {@Nonnull})
public class ExtendedMySQLContainerProvider extends MySQLContainerProvider {

	@Nonnull
	@Override
	@SuppressWarnings(value = "rawtypes")
	public JdbcDatabaseContainer newInstance() {
		return newInstance(ExtendedMySQLContainer.DEFAULT_TAG);
	}

	@Nonnull
	@Override
	@SuppressWarnings(value = {"deprecation", "rawtypes"})
	public JdbcDatabaseContainer newInstance(@Nullable final String tag) {
		@Nonnull final DockerImageName dockerImageName = DockerImageName.parse(ExtendedMySQLContainer.IMAGE).withTag(tag);
		return tag == null
			? newInstance()
			: new ExtendedMySQLContainer(dockerImageName);
	}

	@Override
	public boolean supports(@Nonnull final String databaseType) {
		return databaseType.equals(ExtendedMySQLContainer.SUBPROTOCOL);
	}
}
