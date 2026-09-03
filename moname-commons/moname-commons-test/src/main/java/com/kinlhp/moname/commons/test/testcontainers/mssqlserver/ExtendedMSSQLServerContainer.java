package com.kinlhp.moname.commons.test.testcontainers.mssqlserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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

	@Nonnull
	static final Path CONTAINER_SH_PATH = getContainerShPath();

	@Nonnull
	static final Path CONTAINER_SQL_PATH = getContainerSqlPath();

	@Nonnull
	static final String DEFAULT_TAG = "2022-latest";

	@Nonnull
	static final String MSSQL_PASSWORD = "moname";

	@Nonnull
	static final String MSSQL_USER = MSSQL_PASSWORD;

	@Nonnull
	static final Path RESOURCE_SH_PATH = getResourceShPath();

	@Nonnull
	static final Path RESOURCE_SQL_PATH = getResourceSqlPath();

	@Nonnull
	static final String SH_FILE = "initial-command.sh";

	@Nonnull
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
	@Nonnull
	static final String SUBPROTOCOL = "sqlserverext";

	@Nonnull
	private static final String ACCEPT_EULA = "Y";

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(ExtendedMSSQLServerContainer.class);

	@Nonnull
	private static final String MSSQL_PID = "Developer";

	@Nonnull
	private static final String MSSQL_SA_PASSWORD = "A_Str0ng_Required_Password";

	@Nullable
	private static volatile String databaseName;

	@Nonnull
	private String password = MSSQL_PASSWORD;

	@Nonnull
	private String username = MSSQL_USER;

	public ExtendedMSSQLServerContainer(@Nonnull final DockerImageName dockerImageName) {
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

	@Nonnull
	@Override
	protected String constructUrlParameters(@Nonnull final String startCharacter, @Nonnull final String delimiter,
			@Nonnull final String endCharacter) {
		return JdbcDatabaseUriExtractableContainer.super
				.constructUrlParameters(urlParameters, startCharacter, delimiter, endCharacter)
				.orElse("");
	}

	@Nonnull
	private static Path getContainerPath() {
		@Nonnull final var rootPath = getRootPath();
		return Paths.get(rootPath.toString(), "home", "mssql");
	}

	@Nonnull
	private static Path getContainerShPath() {
		@Nonnull final var containerPath = getContainerPath();
		return Paths.get(containerPath.toString(), SH_FILE);
	}

	@Nonnull
	private static Path getContainerSqlPath() {
		@Nonnull final var containerPath = getContainerPath();
		return Paths.get(containerPath.toString(), SQL_FILE);
	}

	@Nonnull
	@Override
	public String getDatabaseName() {
		if (databaseName == null) {
			synchronized (ExtendedMSSQLServerContainer.class) {
				if (databaseName == null) {
					databaseName = JdbcDatabaseUriExtractableContainer.super.getContainerDatabaseDriverJdbcUri()
							.map(this::getDatabaseName)
							.filter(Optional::isPresent)
							.map(Optional::get)
							.orElse("%s_test".formatted(MONAME));
				}
			}
		}
		//noinspection DataFlowIssue
		return databaseName;
	}

	@Nonnull
	@Override
	public Optional<String> getDatabaseName(@Nonnull final URI jdbcUrl) {
		if (databaseName != null) {
			return Optional.of(databaseName);
		}
		getLogger().trace("Extracting database name from JDBC URL {}", jdbcUrl);
		@Nonnull @SuppressWarnings("java:S1117") final var databaseName = JdbcDatabaseUriExtractableContainer.super
				.getParamValue(jdbcUrl, "databaseName");
		databaseName.ifPresentOrElse(
				extractedDatabaseName -> getLogger().debug("The database name {} was extracted from the JDBC URI {}",
						extractedDatabaseName, jdbcUrl),
				() -> getLogger().warn("The database name could not be extracted from the JDBC URL {}", jdbcUrl)
		);
		return databaseName;
	}

	@Nonnull
	@Override
	public String getDriverClassName() {
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
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
	public String getQueryParamSeparator() {
		return ";";
	}

	@Nonnull
	@Override
	public String getQueryParamStarter() {
		return ";";
	}

	@Nonnull
	private static Path getResourcePath() {
		return Paths.get("db", "sqlserver");
	}

	@Nonnull
	private static Path getResourceShPath() {
		@Nonnull final var resourcePath = getResourcePath();
		return Paths.get(resourcePath.toString(), SH_FILE);
	}

	@Nonnull
	private static Path getResourceSqlPath() {
		@Nonnull final var resourcePath = getResourcePath();
		return Paths.get(resourcePath.toString(), SQL_FILE);
	}

	@Nonnull
	private static Path getRootPath() {
		return Paths.get(System.getProperty("user.dir")).getRoot();
	}

	@Nonnull
	@Override
	public String getSubprotocol() {
		return SUBPROTOCOL;
	}

	@Nonnull
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

	@Nonnull
	private ExtendedMSSQLServerContainer<SELF> withAddExposedPort() {
		super.addExposedPort(MS_SQL_SERVER_PORT);
		return super.self();
	}

	@Override
	public void withAutoResolvedTcPatternUriParameter(@Nonnull final Map.Entry<String, String> parameter) {
		JdbcDatabaseUriExtractableContainer.super.withAutoResolvedTcPatternUriParameter(parameter);
	}

	@Nonnull
	private ExtendedMSSQLServerContainer<SELF> withClasspathResourceMapping() {
		return super.withClasspathResourceMapping(RESOURCE_SH_PATH.toString(), CONTAINER_SH_PATH.toString(), BindMode.READ_ONLY)
				.withClasspathResourceMapping(
						RESOURCE_SQL_PATH.toString(),
						CONTAINER_SQL_PATH.toString(),
						BindMode.READ_ONLY
				);
	}

	@Nonnull
	private ExtendedMSSQLServerContainer<SELF> withCommand() {
		return super.withCommand(commandParts());
	}

	@Nonnull
	private String[] commandParts() {
		// TODO: Use bash -c "$(cat /home/mssql/initial-command.sh)"
		return new String[]{"sh", CONTAINER_SH_PATH.toString()};
	}

	/**
	 * @see ExtendedMSSQLServerContainer#getDatabaseName() getDatabaseName
	 */
	@Nonnull
	@Override
	public SELF withDatabaseName(@Nonnull final String databaseName) {
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
	@Nonnull
	private ExtendedMSSQLServerContainer<SELF> withEnv() {
		return super.withEnv("ACCEPT_EULA", ACCEPT_EULA)
				.withEnv("MSSQL_DATABASE", getDatabaseName())
				.withEnv("MSSQL_PASSWORD", getPassword())
				.withEnv("MSSQL_PID", MSSQL_PID)
				.withEnv("MSSQL_SA_PASSWORD", MSSQL_SA_PASSWORD)
				.withEnv("MSSQL_USER", getUsername());
	}

	@Nonnull
	private ExtendedMSSQLServerContainer<SELF> withNetworkAliases() {
		return super.withNetworkAliases(SQLSERVER);
	}

	@Nonnull
	@Override
	public SELF withPassword(@Nonnull final String password) {
		this.password = password;
		return super.self();
	}

	@Nonnull
	@Override
	public SELF withUsername(@Nonnull final String username) {
		this.username = username;
		return super.self();
	}
}
