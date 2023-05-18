package com.kinlhp.moname.commons.test.testcontainers;

import jakarta.annotation.Nonnull;

import org.testcontainers.containers.OracleContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Extended Oracle container.
 * @see OracleContainer
 */
public class ExtendedOracleContainer extends OracleContainer {

	@Nonnull
	static final String DEFAULT_TAG = "21.3.0-slim-faststart";
	/**
	 * <p>
	 * The format of a JDBC URL is:
	 * <ul>
	 * <li>{@literal jdbc:<subprotocol>:<subname>}</li></ul>
	 * <p>
	 * where <i>subprotocol</i> defines the kind of database connectivity
	 * mechanism that may be supported by one or more drivers. The contents and
	 * syntax of the <i>subname</i> will depend on the <i>subprotocol</i>.
	 * <p>
	 * <strong>NOTE:</strong> A JDBC URL is not required to fully adhere to the
	 * URI syntax as defined in RFC 3986, Uniform Resource Identifier (URI):
	 * Generic Syntax.
	 */
	@Nonnull
	static final String SUBPROTOCOL = "oracleext";
	@Nonnull
	private static final String APP_USER = "moname";
	@Nonnull
	private static final String APP_USER_PASSWORD = APP_USER;

	public ExtendedOracleContainer(@Nonnull final DockerImageName dockerImageName) {
		super(dockerImageName);
		super.logger().info("Extended Oracle container for \":{}\" JDBC subprotocol.", SUBPROTOCOL);
	}

	@Override
	protected void configure() {
		super.configure();
		withEnv();
	}

	@Nonnull
	@Override
	public String getDriverClassName() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	@Nonnull
	@Override
	public String getPassword() {
		return APP_USER_PASSWORD;
	}

	@Nonnull
	@Override
	public String getUsername() {
		// An application user is tied to the database, and therefore not
		// authenticated to connect to SID.
		return APP_USER;
	}

	private void withEnv() {
		super.withEnv("APP_USER", APP_USER)
			.withEnv("APP_USER_PASSWORD", APP_USER_PASSWORD)
			.withEnv("ORACLE_DATABASE", getDatabaseName());
	}

	@Override
	protected void waitUntilContainerStarted() {
		super.logger().info("Waiting for database connection to become available at {} using query '{}'", getJdbcUrl(), getTestQueryString());
		super.waitUntilContainerStarted();
		super.logger().info("Container is started (JDBC URL: {})", getJdbcUrl());
	}
}
