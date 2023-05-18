package com.kinlhp.moname.commons.test.testcontainers;

import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Extended MS SQL Server container.
 * @see MSSQLServerContainer
 */
public class ExtendedMSSQLServerContainer<SELF extends ExtendedMSSQLServerContainer<SELF>> extends MSSQLServerContainer<SELF> {

	@Nonnull
	static final String CONTAINER_PATH_SH = "/home/mssql/initial-command.sh";
	@Nonnull
	static final String CONTAINER_PATH_SQL = "/home/mssql/initial-setup.sql";
	@Nonnull
	static final String DEFAULT_TAG = "2022-latest";
	@Nonnull
	static final String MSSQL_PASSWORD = "moname";
	@Nonnull
	static final String MSSQL_USER = MSSQL_PASSWORD;
	@Nonnull
	static final String RESOURCE_PATH_SH = "db/sqlserver/initial-command.sh";
	@Nonnull
	static final String RESOURCE_PATH_SQL = "db/sqlserver/initial-setup.sql";
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
	static final String SUBPROTOCOL = "sqlserverext";
	@Nonnull
	private static final String ACCEPT_EULA = "Y";
	@Nonnull
	private static final String MSSQL_PID = "Developer";
	@Nonnull
	private static final String MSSQL_SA_PASSWORD = "A_Str0ng_Required_Password";
	@SuppressWarnings(value = "NotNullFieldNotInitialized")
	@Nonnull
	private String databaseName;
	@Nonnull
	private String password = MSSQL_PASSWORD;
	@Nonnull
	private String username = MSSQL_USER;

	public ExtendedMSSQLServerContainer(@Nonnull final DockerImageName dockerImageName) {
		super(dockerImageName);
		super.logger().info("Extended MS SQL Server container for \":{}\" JDBC subprotocol.", SUBPROTOCOL);
	}

	@Nonnull
	private String[] commandParts() {
		// TODO: Use sh -c "$(cat /home/mssql/initial-command.sh)" instead:
		return new String[]{
			"sh",
			"-c",
			"sleep 5s && /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P " + MSSQL_SA_PASSWORD + " -d master -i " + CONTAINER_PATH_SQL + " & /opt/mssql/bin/sqlservr"
		};
	}

	@Override
	protected void configure() {
		super.configure();
		with();
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
	public String getDatabaseName() {
		return databaseName;
	}

	@Nonnull
	@Override
	public String getDriverClassName() {
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}

	@Nonnull
	@Override
	public String getPassword() {
		return password;
	}

	@Nonnull
	@Override
	public String getUsername() {
		return username;
	}

	private void with() {
		super.withClasspathResourceMapping(RESOURCE_PATH_SH, CONTAINER_PATH_SH, BindMode.READ_ONLY)
			.withClasspathResourceMapping(RESOURCE_PATH_SQL, CONTAINER_PATH_SQL, BindMode.READ_ONLY)
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

	@Nonnull
	@Override
	public SELF withDatabaseName(@Nonnull final String databaseName) {
		this.databaseName = databaseName;
		return super.self();
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
