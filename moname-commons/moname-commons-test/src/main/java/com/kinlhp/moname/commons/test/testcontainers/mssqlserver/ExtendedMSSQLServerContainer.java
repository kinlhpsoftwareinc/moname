package com.kinlhp.moname.commons.test.testcontainers.mssqlserver;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import com.kinlhp.moname.commons.test.testcontainers.JdbcDatabaseUriExtractableContainer;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MONAME;
import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.SQLSERVER;

/**
 * Extended MS SQL Server container.
 *
 * @see MSSQLServerContainer MSSQLServerContainer
 */
@SuppressWarnings({"java:S119", "java:S2160"})
public class ExtendedMSSQLServerContainer<SELF extends ExtendedMSSQLServerContainer<SELF>>
		extends MSSQLServerContainer<SELF>
		implements JdbcDatabaseUriExtractableContainer<SELF> {

	static final Path CONTAINER_SH_PATH = getContainerShPath();

	static final Path CONTAINER_SQL_PATH = getContainerSqlPath();

	static final String DEFAULT_TAG = "2022-latest";

	static final String MSSQL_PASSWORD = "moname";

	static final String MSSQL_USER = MSSQL_PASSWORD;

	static final Path RESOURCE_SH_PATH = getResourceShPath();

	static final Path RESOURCE_SQL_PATH = getResourceSqlPath();

	static final String SH_FILE = "initial-command.sh";

	static final String SQL_FILE = "initial-setup.sql";

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
	static final String SUBPROTOCOL = "sqlserverext";

	private static final String ACCEPT_EULA = "Y";

	private static final Logger LOG = LoggerFactory.getLogger(ExtendedMSSQLServerContainer.class);

	private static final String MSSQL_PID = "Developer";

	private static final String MSSQL_SA_PASSWORD = "A_Str0ng_Required_Password";

	/**
	 * Safe private lock-splitting approach.
	 */
	private static final DatabaseNamePrivateLock DATABASE_NAME_PRIVATE_LOCK = new DatabaseNamePrivateLock();

	private static volatile @Nullable String databaseName;

	private String password = MSSQL_PASSWORD;

	private String username = MSSQL_USER;

	public ExtendedMSSQLServerContainer(final DockerImageName dockerImageName) {
		super(dockerImageName);
		LOG.info("Extended MS SQL Server container for \"jdbc:tc:{}\" JDBC subprotocol", SUBPROTOCOL);
	}

	@Override
	protected void configure() {
		// TODO: https://www.docker.com/blog/testcontainers-best-practices
		super.configure();
		//with();
		//noinspection resource
		withAddExposedPort()
				.withClasspathResourceMapping()
				.withCommand()
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

	private static Path getContainerPath() {
		final var rootPath = getRootPath();
		return Paths.get(rootPath.toString(), "home", "mssql");
	}

	private static Path getContainerShPath() {
		final var containerPath = getContainerPath();
		return Paths.get(containerPath.toString(), SH_FILE);
	}

	private static Path getContainerSqlPath() {
		final var containerPath = getContainerPath();
		return Paths.get(containerPath.toString(), SQL_FILE);
	}

	@Override
	public String getDatabaseName() {
		// The local variable `readingReducer` is not merely cosmetic. It reduces the number of reads of the volatile
		// field from two or three to one on the fast path. It is a micro-optimization, but it comes for free.
		var readingReducer = databaseName;
		if (readingReducer == null) {
			synchronized (DATABASE_NAME_PRIVATE_LOCK) {
				readingReducer = databaseName;
				if (readingReducer == null) {
					try {
						readingReducer = JdbcDatabaseUriExtractableContainer.super.getContainerDatabaseDriverJdbcUri()
								.map(this::getDatabaseName)
								.filter(Optional::isPresent)
								.map(Optional::get)
								.orElse("%s_test".formatted(MONAME));
						databaseName = readingReducer;
					} catch (final Exception exception) {
						databaseName = null;
						throw exception;
					}
				}
			}
		}
		return readingReducer;
	}

	@Override
	public Optional<String> getDatabaseName(final URI jdbcUrl) {
		if (databaseName != null) {
			return Optional.of(databaseName);
		}
		getLogger().trace("Extracting database name from JDBC URL {}", jdbcUrl);
		final @SuppressWarnings("java:S1117") var databaseName = JdbcDatabaseUriExtractableContainer.super
				.getParamValue(jdbcUrl, "databaseName");
		databaseName.ifPresentOrElse(
				extractedDatabaseName -> getLogger().debug("The database name {} was extracted from the JDBC URI {}",
						extractedDatabaseName, jdbcUrl),
				() -> getLogger().warn("The database name could not be extracted from the JDBC URL {}", jdbcUrl)
		);
		return databaseName;
	}

	@Override
	public String getDriverClassName() {
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}

	@Override
	public Logger getLogger() {
		return LOG;
	}

	@Override
	public String getPassword() {
		return getUsername();
	}

	@Override
	public String getQueryParamSeparator() {
		return ";";
	}

	@Override
	public String getQueryParamStarter() {
		return ";";
	}

	private static Path getResourcePath() {
		return Paths.get("db", "sqlserver");
	}

	private static Path getResourceShPath() {
		final var resourcePath = getResourcePath();
		return Paths.get(resourcePath.toString(), SH_FILE);
	}

	private static Path getResourceSqlPath() {
		final var resourcePath = getResourcePath();
		return Paths.get(resourcePath.toString(), SQL_FILE);
	}

	private static Path getRootPath() {
		return Paths.get(System.getProperty("user.dir")).getRoot();
	}

	@Override
	public String getSubprotocol() {
		return SUBPROTOCOL;
	}

	@Override
	@SuppressWarnings("java:S4275")
	public String getUsername() {
		return MSSQL_USER;
	}

	/*
	private void with() {
		withClasspathResourceMapping(RESOURCE_SH_PATH.toString(), CONTAINER_SH_PATH.toString(), BindMode.READ_ONLY)
				.withClasspathResourceMapping(
						RESOURCE_SQL_PATH.toString(),
						CONTAINER_SQL_PATH.toString(),
						BindMode.READ_ONLY
				)
				.withCommand(commandParts())
				.withEnv("ACCEPT_EULA", ACCEPT_EULA)
				.withEnv("MSSQL_DATABASE", getDatabaseName())
				.withEnv("MSSQL_PASSWORD", getPassword())
				.withEnv("MSSQL_PID", MSSQL_PID)
				.withEnv("MSSQL_SA_PASSWORD", MSSQL_SA_PASSWORD)
				.withEnv("MSSQL_USER", getUsername())
				.withUrlParam("databaseName", getDatabaseName())
				.withUrlParam("sendStringParametersAsUnicode", "false");
	}
	 */

	private ExtendedMSSQLServerContainer<SELF> withAddExposedPort() {
		super.addExposedPort(MS_SQL_SERVER_PORT);
		return super.self();
	}

	@Override
	public void withAutoResolvedTcPatternUriParameter(final Map.Entry<String, String> parameter) {
		JdbcDatabaseUriExtractableContainer.super.withAutoResolvedTcPatternUriParameter(parameter);
	}

	private ExtendedMSSQLServerContainer<SELF> withClasspathResourceMapping() {
		return super.withClasspathResourceMapping(RESOURCE_SH_PATH.toString(), CONTAINER_SH_PATH.toString(), BindMode.READ_ONLY)
				.withClasspathResourceMapping(
						RESOURCE_SQL_PATH.toString(),
						CONTAINER_SQL_PATH.toString(),
						BindMode.READ_ONLY
				);
	}

	private ExtendedMSSQLServerContainer<SELF> withCommand() {
		return super.withCommand(commandParts());
	}

	private String[] commandParts() {
		// TODO: Use bash -c "$(cat /home/mssql/initial-command.sh)"
		return new String[]{"sh", CONTAINER_SH_PATH.toString()};
	}

	/**
	 * @see ExtendedMSSQLServerContainer#getDatabaseName() getDatabaseName
	 */
	@Override
	public SELF withDatabaseName(final String databaseName) {
		LOG.warn("""
				Parent class org.testcontainers.containers.MSSQLServerContainer does not implement support for method \
				withDatabaseName(String)""");
		return super.self();
	}

	/**
	 * <a href="https://learn.microsoft.com/en-us/sql/linux/configure/environment-variables?view=sql-server-ver16">
	 * Configure SQL Server settings with environment variables on Linux
	 * </a>
	 */
	private ExtendedMSSQLServerContainer<SELF> withEnv() {
		return super.withEnv("ACCEPT_EULA", ACCEPT_EULA)
				.withEnv("MSSQL_DATABASE", getDatabaseName())
				.withEnv("MSSQL_PASSWORD", getPassword())
				.withEnv("MSSQL_PID", MSSQL_PID)
				.withEnv("MSSQL_SA_PASSWORD", MSSQL_SA_PASSWORD)
				.withEnv("MSSQL_USER", getUsername());
	}

	private ExtendedMSSQLServerContainer<SELF> withNetworkAliases() {
		return super.withNetworkAliases(SQLSERVER);
	}

	@Override
	public SELF withPassword(final String password) {
		this.password = password;
		return super.self();
	}

	@Override
	public SELF withUsername(final String username) {
		this.username = username;
		return super.self();
	}

	/**
	 * Safe private lock-splitting approach.
	 *
	 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">
	 * The "Double-Checked Locking is Broken" Declaration
	 * </a>
	 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html">JSR 133 (Java Memory Model) FAQ</a>
	 * @see <a href="https://docs.oracle.com/javase/specs/jls/se24/html/jls-17.html">Chapter 17. Threads and Locks</a>
	 * @see <a href="https://jcp.org/en/jsr/detail?id=133">
	 * JSR 133: JavaTM Memory Model and Thread Specification Revision
	 * </a>
	 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel">The Java Memory Model</a>
	 */
	@SuppressWarnings("java:S2094")
	private static final class DatabaseNamePrivateLock {
	}
}
