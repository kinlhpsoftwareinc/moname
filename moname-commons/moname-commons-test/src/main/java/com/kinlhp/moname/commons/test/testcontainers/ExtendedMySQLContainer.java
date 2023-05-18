package com.kinlhp.moname.commons.test.testcontainers;

import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Extended MySQL container.
 * @see MySQLContainer
 */
public class ExtendedMySQLContainer<SELF extends ExtendedMySQLContainer<SELF>> extends MySQLContainer<SELF> {

	@Nonnull
	static final String DEFAULT_TAG = "8.0.33";
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
	static final String SUBPROTOCOL = "mysqlext";
	@Nonnull
	private static final String MYSQL_PASSWORD = "moname";
	@Nonnull
	private static final String MYSQL_USER = MYSQL_PASSWORD;

	public ExtendedMySQLContainer(@Nonnull final DockerImageName dockerImageName) {
		super(dockerImageName);
		super.logger().info("Extended MySQL container for \":{}\" JDBC subprotocol.", SUBPROTOCOL);
	}

	@Override
	protected void configure() {
		super.configure();
		withEnv();
		withUrlParam();
	}

	@Nonnull
	@Override
	protected String constructUrlParameters(@Nonnull final String startCharacter, @Nonnull final String delimiter, @Nonnull final String endCharacter) {
		return urlParameters.isEmpty()
			? ""
			: startCharacter
			+ urlParameters.entrySet().stream()
			.map(Object::toString)
			.sorted()
			.collect(Collectors.joining(delimiter))
			+ endCharacter;
	}

	@Nonnull
	@Override
	public String getDriverClassName() {
		return "com.mysql.cj.jdbc.Driver";
	}

	@Nonnull
	@Override
	public String getPassword() {
		return MYSQL_PASSWORD;
	}

	@Nonnull
	@Override
	public String getUsername() {
		return MYSQL_USER;
	}

	private void withEnv() {
		super.withEnv("MYSQL_DATABASE", getDatabaseName())
			.withEnv("MYSQL_PASSWORD", getPassword())
			.withEnv("MYSQL_ROOT_PASSWORD", getPassword())
			.withEnv("MYSQL_USER", getUsername());
	}

	private void withUrlParam() {
		super.withUrlParam("cachePrepStmts", "true")
			.withUrlParam("cacheResultSetMetadata", "true")
			.withUrlParam("cacheServerConfiguration", "true")
			.withUrlParam("characterEncoding", "UTF-8")
			.withUrlParam("connectionCollation", "utf8mb4_general_ci")
			.withUrlParam("connectionTimeZone", "UTC")
			.withUrlParam("createDatabaseIfNotExist", "true")
			.withUrlParam("elideSetAutoCommits", "true")
			.withUrlParam("maintainTimeStats", "false")
			.withUrlParam("prepStmtCacheSize", "256")
			.withUrlParam("prepStmtCacheSqlLimit", "2048")
			.withUrlParam("rewriteBatchedStatements", "true")
			.withUrlParam("useLocalSessionState", "true")
			.withUrlParam("useServerPrepStmts", "true");
	}
}
