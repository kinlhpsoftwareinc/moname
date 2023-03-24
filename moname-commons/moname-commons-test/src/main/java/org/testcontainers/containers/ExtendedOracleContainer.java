package org.testcontainers.containers;

import org.testcontainers.utility.DockerImageName;

/**
 * Extended Oracle container. See {@link OracleContainer} for details.
 */
public class ExtendedOracleContainer extends OracleContainer {

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
	static final String SUBPROTOCOL = "oracleext";
	static final String DEFAULT_TAG = "21.3.0-slim-faststart";
	private static final String APP_USER = "moname";
	private static final String APP_USER_PASSWORD = APP_USER;

	public ExtendedOracleContainer(final DockerImageName dockerImageName) {
		super(dockerImageName);
		logger().info("Extended Oracle container for \":{}\" JDBC subprotocol.",
			SUBPROTOCOL);
	}

	@Override
	protected void waitUntilContainerStarted() {
		logger().info("Waiting for database connection to become available at {} using query '{}'",
			getJdbcUrl(), getTestQueryString());
		super.waitUntilContainerStarted();
		logger().info("Container is started (JDBC URL: {})", getJdbcUrl());
	}

	@Override
	public String getDriverClassName() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	@Override
	public String getUsername() {
		// An application user is tied to the database, and therefore not
		// authenticated to connect to SID.
		return APP_USER;
	}

	@Override
	public String getPassword() {
		return APP_USER_PASSWORD;
	}

	@Override
	protected void configure() {
		super.configure();
		withEnv();
	}

	private void withEnv() {
		super.withEnv("ORACLE_DATABASE", getDatabaseName())
			.withEnv("APP_USER", APP_USER)
			.withEnv("APP_USER_PASSWORD", APP_USER_PASSWORD);
	}

}
