package org.testcontainers.containers;

import java.util.stream.Collectors;

import org.testcontainers.utility.DockerImageName;

/**
 * Extended MS SQL Server container. See {@link MSSQLServerContainer} for details.
 */
public class ExtendedMSSQLServerContainer<SELF extends ExtendedMSSQLServerContainer<SELF>>
	extends MSSQLServerContainer<SELF> {

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
	static final String SUBPROTOCOL = "sqlserverext";
	static final String DEFAULT_TAG = "2022-latest";
	private static final String ACCEPT_EULA = "Y";
	private static final String MSSQL_PID = "Developer";
	private static final String MSSQL_SA_PASSWORD = "A_Str0ng_Required_Password";
	static final String MSSQL_USER = "moname";
	static final String MSSQL_PASSWORD = MSSQL_USER;
	static final String RESOURCE_PATH_SH = "db/sqlserver/initial-command.sh";
	static final String CONTAINER_PATH_SH = "/home/mssql/initial-command.sh";
	static final String RESOURCE_PATH_SQL = "db/sqlserver/initial-setup.sql";
	static final String CONTAINER_PATH_SQL = "/home/mssql/initial-setup.sql";
    private String databaseName;
    private String username = MSSQL_USER;
    private String password = MSSQL_PASSWORD;

	public ExtendedMSSQLServerContainer(final DockerImageName dockerImageName) {
		super(dockerImageName);
		logger().info("Extended MS SQL Server container for \":{}\" JDBC subprotocol.",
			SUBPROTOCOL);
	}

	@Override
	public String getDriverClassName() {
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}

	@Override
	protected String constructUrlParameters(final String startCharacter,
		final String delimiter, final String endCharacter) {
		return urlParameters.isEmpty()
			? ""
			: startCharacter
			+ urlParameters.entrySet().stream()
			.map(Object::toString)
			.sorted()
			.collect(Collectors.joining(delimiter))
			+ endCharacter;
	}

	@Override
	public String getDatabaseName() {
		return this.databaseName;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	protected void configure() {
		super.configure();
		with();
	}

    @Override
    public SELF withDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
        return self();
    }

    @Override
    public SELF withUsername(final String username) {
        this.username = username;
        return self();
    }

    @Override
    public SELF withPassword(final String password) {
        this.password = password;
        return self();
    }

	private void with() {
		super.withEnv("ACCEPT_EULA", ACCEPT_EULA)
			.withEnv("MSSQL_DATABASE", getDatabaseName())
			.withEnv("MSSQL_PASSWORD", getPassword())
			.withEnv("MSSQL_PID", MSSQL_PID)
			.withEnv("MSSQL_SA_PASSWORD", MSSQL_SA_PASSWORD)
			.withEnv("MSSQL_USER", getUsername())
			.withClasspathResourceMapping(RESOURCE_PATH_SH, CONTAINER_PATH_SH, BindMode.READ_ONLY)
			.withClasspathResourceMapping(RESOURCE_PATH_SQL, CONTAINER_PATH_SQL, BindMode.READ_ONLY)
			.withCommand("sh", "-c", initialCommand())
			.withUrlParam("databaseName", getDatabaseName())
			.withUrlParam("sendStringParametersAsUnicode", "false");
	}

	private String initialCommand() {
		//TODO: Use sh -c "$(cat /home/mssql/initial-command.sh)" instead.
		return "sleep 5s && /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "
			+ MSSQL_SA_PASSWORD  + " -d master -i " + CONTAINER_PATH_SQL
			+ " & /opt/mssql/bin/sqlservr";
	}

}
