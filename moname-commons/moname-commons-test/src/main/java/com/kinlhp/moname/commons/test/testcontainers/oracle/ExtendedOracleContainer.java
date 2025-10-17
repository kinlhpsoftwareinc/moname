package com.kinlhp.moname.commons.test.testcontainers.oracle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import com.kinlhp.moname.commons.test.testcontainers.JdbcDatabaseUriExtractableContainer;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.ORACLE;

/**
 * Extended Oracle container.
 *
 * @see OracleContainer OracleContainer
 */
@SuppressWarnings("java:S119")
public class ExtendedOracleContainer<SELF extends JdbcDatabaseContainer<SELF>
		& JdbcDatabaseUriExtractableContainer<SELF>>
		extends OracleContainer
		implements JdbcDatabaseUriExtractableContainer<SELF> {

	public static final int ORACLE_PORT = 1521;

	static final String DEFAULT_TAG = "23.26.3-slim-faststart";

	/**
	 * <p>
	 * The format of a JDBC URL is:
	 * <ul>
	 * <li>{@literal jdbc:<subprotocol>:<subname>}</li>
	 * </ul>
	 * <p>
	 * where <i>subprotocol</i> defines the kind of database connectivity mechanism that may be supported by one or
	 * more drivers. The contents and syntax of the <i>subname</i> will depend on the <i>subprotocol</i>.
	 * <p>
	 * <strong>NOTE:</strong> A JDBC URL is not required to fully adhere to the URI syntax as defined in RFC 3986,
	 * Uniform Resource Identifier (URI): Generic Syntax.
	 */
	static final String SUBPROTOCOL = "oracleext";

	private static final String APP_USER = "moname";

	@SuppressWarnings("java:S2068")
	private static final String APP_USER_PASSWORD = APP_USER;

	private static final Logger LOG = LoggerFactory.getLogger(ExtendedOracleContainer.class);

	public ExtendedOracleContainer(final DockerImageName dockerImageName) {
		super(dockerImageName);
		LOG.info("Extended Oracle container for \"jdbc:tc:{}\" JDBC subprotocol", SUBPROTOCOL);
	}

	@Override
	protected void configure() {
		// TODO: https://www.docker.com/blog/testcontainers-best-practices
		super.configure();
		//noinspection resource
		withAddExposedPort()
				.withEnv()
				.withNetworkAliases()
				//--
				.withDatabaseName()
				.withAutoResolveUriParameters()
				.withAutoResolveTcPatternUriParameters();
	}

	@Override
	protected String constructUrlParameters(final String startCharacter, final String delimiter,
			final String endCharacter) {
		return JdbcDatabaseUriExtractableContainer.super
				.constructUrlParameters(urlParameters, startCharacter, delimiter, endCharacter)
				.orElse("");
	}

	@Override
	public String getDriverClassName() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	@Override
	public Logger getLogger() {
		return LOG;
	}

	@Override
	public String getPassword() {
		return APP_USER_PASSWORD;
	}

	@Override
	public String getSubprotocol() {
		return SUBPROTOCOL;
	}

	@Override
	public String getUsername() {
		// An application user is tied to the database, and therefore not
		// authenticated to connect to SID.
		return APP_USER;
	}

	private ExtendedOracleContainer<SELF> withAddExposedPort() {
		super.addExposedPort(ORACLE_PORT);
		return this;
	}

	@Override
	public void withAutoResolvedTcPatternUriParameter(final Map.Entry<String, String> parameter) {
		JdbcDatabaseUriExtractableContainer.super.withAutoResolvedTcPatternUriParameter(parameter);
	}

	private ExtendedOracleContainer<SELF> withEnv() {
		super.withEnv("APP_USER", getUsername())
				.withEnv("APP_USER_PASSWORD", getPassword())
				.withEnv("ORACLE_DATABASE", getDatabaseName());
		return this;
	}

	private ExtendedOracleContainer<SELF> withNetworkAliases() {
		super.withNetworkAliases(ORACLE);
		return this;
	}

	@Override
	protected void waitUntilContainerStarted() {
		LOG.info("Waiting for database connection to become available at {} using query '{}'", getJdbcUrl(),
				getTestQueryString());
		super.waitUntilContainerStarted();
		LOG.info("Container is started (JDBC URL: {})", getJdbcUrl());
	}
}
