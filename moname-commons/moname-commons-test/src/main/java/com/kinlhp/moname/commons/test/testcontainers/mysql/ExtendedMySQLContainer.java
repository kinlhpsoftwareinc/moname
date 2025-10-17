package com.kinlhp.moname.commons.test.testcontainers.mysql;

import com.github.dockerjava.api.command.CreateContainerCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import jakarta.annotation.Nonnull;

import java.util.Map;

import com.kinlhp.moname.commons.test.testcontainers.JdbcDatabaseUriExtractableContainer;
import com.kinlhp.moname.commons.test.testcontainers.SharedNetwork;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MONAME;
import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MYSQL;

/**
 * Extended MySQL container.
 *
 * @see MySQLContainer MySQLContainer
 */
@SuppressWarnings("java:S119")
public class ExtendedMySQLContainer<SELF extends ExtendedMySQLContainer<SELF>>
		extends MySQLContainer<SELF>
		implements JdbcDatabaseUriExtractableContainer<SELF> {

	@Nonnull
	static final String DEFAULT_TAG = "9.2.0";

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
	@Nonnull
	static final String SUBPROTOCOL = MYSQL + "ext";

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(ExtendedMySQLContainer.class);

	@Nonnull
	private static final String MYSQL_USER = MONAME;

	@Nonnull
	private static final String SQL_FILE = "initial-setup.sql";

	public ExtendedMySQLContainer(@Nonnull final DockerImageName dockerImageName) {
		super(dockerImageName);
		LOG.info("Extended MySQL container for \"jdbc:tc:{}\" JDBC subprotocol", SUBPROTOCOL);
	}

	@Override
	protected void configure() {
		// TODO: https://www.docker.com/blog/testcontainers-best-practices
		super.configure();
		//noinspection resource
		withAddExposedPort()
				.withCopyFileToContainer()
				.withCreateContainerCmdModifier()
				.withEnv()
				.withNetwork()
				.withNetworkAliases()
				//--
				.withDatabaseName()
				.withAutoResolveUriParameters()
				.withAutoResolveTcPatternUriParameters();
	}

	@Nonnull
	@Override
	protected String constructUrlParameters(@Nonnull final String startCharacter, @Nonnull final String delimiter,
			@Nonnull final String endCharacter) {
		return JdbcDatabaseUriExtractableContainer.super
				.constructUrlParameters(urlParameters, startCharacter, delimiter, endCharacter)
				.orElse("");
		//return urlParameters.isEmpty()
		//		? ""
		//		: sortParams(urlParameters, startCharacter, delimiter, endCharacter);
	}

	//@Nonnull
	//private String sortUrlParameters(@Nonnull final String startCharacter, @Nonnull final String delimiter,
	//		@Nonnull final String endCharacter) {
	//	@Nonnull final var sortedUrlParameters = urlParameters.entrySet().stream()
	//			.map(Object::toString)
	//			.sorted()
	//			.collect(Collectors.joining(delimiter));
	//	return "%s%s%s".formatted(startCharacter, sortedUrlParameters, endCharacter);
	//}

	@Nonnull
	@Override
	public String getDriverClassName() {
		return "com.mysql.cj.jdbc.Driver";
	}

	@Nonnull
	@Override
	public Logger getLogger() {
		return LOG;
	}

	@Nonnull
	@Override
	public String getPassword() {
		return getUsername();
	}

	@Nonnull
	@Override
	public String getSubprotocol() {
		return SUBPROTOCOL;
	}

	@Nonnull
	@Override
	public String getUsername() {
		return MYSQL_USER;
	}

	@Nonnull
	private ExtendedMySQLContainer<SELF> withAddExposedPort() {
		super.addExposedPort(MYSQL_PORT);
		return super.self();
	}

	@Override
	public void withAutoResolvedTcPatternUriParameter(@Nonnull final Map.Entry<String, String> parameter) {
		JdbcDatabaseUriExtractableContainer.super.withAutoResolvedTcPatternUriParameter(parameter);
	}

	/**
	 * <a href=https://dev.mysql.com/doc/refman/9.2/en/docker-mysql-more-topics.html#docker-additional-init>
	 * Running Additional Initialization Scripts
	 * </a>
	 */
	@Nonnull
	private ExtendedMySQLContainer<SELF> withCopyFileToContainer() {
		return super.withCopyFileToContainer(
				MountableFile.forClasspathResource("db/%s/%s".formatted(MYSQL, SQL_FILE), 444),
				"/docker-entrypoint-initdb.d/%s".formatted(SQL_FILE)
		);
	}

	@Nonnull
	private ExtendedMySQLContainer<SELF> withCreateContainerCmdModifier() {
		return super.withCreateContainerCmdModifier(this::withCreateContainerCmdModifier);
	}

	private void withCreateContainerCmdModifier(@Nonnull final CreateContainerCmd createContainerCmd) {
		createContainerCmd.withHostName(MYSQL);
	}

	//@Nonnull
	//@Override
	//public SELF withDatabaseName() {
	//	return MemoryConfigurableUriExtractableJdbcDatabaseContainer.super.withDatabaseName();
	//}

	@Nonnull
	private ExtendedMySQLContainer<SELF> withEnv() {
		return super.withEnv("MYSQL_DATABASE", super.getDatabaseName())
				.withEnv("MYSQL_INITDB_SKIP_TZINFO", "false")
				.withEnv("MYSQL_PASSWORD", getPassword())
				.withEnv("MYSQL_ROOT_HOST", MYSQL)
				.withEnv("MYSQL_ROOT_PASSWORD", getPassword())
				.withEnv("MYSQL_USER", getUsername());
	}

	//@Nonnull
	//@Override
	//public SELF withMemory() {
	//	return MemoryConfigurableUriExtractableJdbcDatabaseContainer.super.withMemory();
	//}

	@Nonnull
	private ExtendedMySQLContainer<SELF> withNetwork() {
		return super.withNetwork(SharedNetwork.getSingleton().getNetwork());
	}

	@Nonnull
	private ExtendedMySQLContainer<SELF> withNetworkAliases() {
		return super.withNetworkAliases(MYSQL);
	}

	//@Nonnull
	//@SuppressWarnings("UnusedReturnValue")
	//private ExtendedMySQLContainer<SELF> withUrlParam() {
	//	// TODO: Get from `super.getJdbcUrl()` parameters
	//	return super.withUrlParam("cachePrepStmts", "true")
	//			.withUrlParam("cacheResultSetMetadata", "true")
	//			.withUrlParam("cacheServerConfiguration", "true")
	//			.withUrlParam("characterEncoding", "UTF-8")
	//			.withUrlParam("connectionCollation", "utf8mb4_general_ci")
	//			.withUrlParam("connectionTimeZone", "UTC")
	//			.withUrlParam("createDatabaseIfNotExist", "true")
	//			.withUrlParam("elideSetAutoCommits", "true")
	//			.withUrlParam("maintainTimeStats", "false")
	//			.withUrlParam("prepStmtCacheSize", "256")
	//			.withUrlParam("prepStmtCacheSqlLimit", "2048")
	//			.withUrlParam("rewriteBatchedStatements", "true")
	//			.withUrlParam("useLocalSessionState", "true")
	//			.withUrlParam("useServerPrepStmts", "true");
	//}
}
