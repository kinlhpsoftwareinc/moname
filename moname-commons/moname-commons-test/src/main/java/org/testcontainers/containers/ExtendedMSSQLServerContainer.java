package org.testcontainers.containers;

import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.utility.DockerImageName;

/**
 * Extended MS SQL Server container. See {@link MSSQLServerContainer} for details.
 */
@Slf4j
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
	static final String MSSQL_DATABASE = "moname_commons_jpa_test";
	static final String MSSQL_USER = "moname";
	static final String MSSQL_PASSWORD = MSSQL_USER;
	static final String RESOURCE_PATH_SH = "db/sqlserver/initial-command.sh";
	static final String CONTAINER_PATH_SH = "/home/mssql/initial-command.sh";
	static final String RESOURCE_PATH_SQL = "db/sqlserver/initial-setup.sql";
	static final String CONTAINER_PATH_SQL = "/home/mssql/initial-setup.sql";

	public ExtendedMSSQLServerContainer(final DockerImageName dockerImageName) {
		super(dockerImageName);
		LOG.info("Extended MS SQL Server container for \":{}\" JDBC subprotocol.",
			SUBPROTOCOL);
	}

	@Override
	public String getDriverClassName() {
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}

	@Override
	protected String constructUrlParameters(final String startCharacter,
		final String delimiter, final String endCharacter) {
		return super.urlParameters.isEmpty()
			? ""
			: startCharacter
			+ super.urlParameters.entrySet().stream()
			.map(Object::toString)
			.sorted()
			.collect(Collectors.joining(delimiter))
			+ endCharacter;
	}

	@Override
	public String getDatabaseName() {
		return MSSQL_DATABASE;
	}

	@Override
	public String getUsername() {
		return MSSQL_USER;
	}

	@Override
	public String getPassword() {
		return MSSQL_PASSWORD;
	}

	@Override
	protected void configure() {
		super.configure();
		super.withEnv("ACCEPT_EULA", ACCEPT_EULA)
			.withEnv("MSSQL_DATABASE", MSSQL_DATABASE)
			.withEnv("MSSQL_PASSWORD", MSSQL_PASSWORD)
			.withEnv("MSSQL_PID", MSSQL_PID)
			.withEnv("MSSQL_SA_PASSWORD", MSSQL_SA_PASSWORD)
			.withEnv("MSSQL_USER", MSSQL_USER)
			.withClasspathResourceMapping(RESOURCE_PATH_SH, CONTAINER_PATH_SH, BindMode.READ_ONLY)
			.withClasspathResourceMapping(RESOURCE_PATH_SQL, CONTAINER_PATH_SQL, BindMode.READ_ONLY)
			.withCommand("sh", "-c", initialCommand())
			.withUrlParam("databaseName", MSSQL_DATABASE)
			.withUrlParam("sendStringParametersAsUnicode", "false");
	}

	private String initialCommand() {
		//TODO: Use sh -c "$(cat /home/mssql/initial-command.sh)" instead.
		return "sleep 5s && /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "
			+ MSSQL_SA_PASSWORD  + " -d master -i " + CONTAINER_PATH_SQL
			+ " & /opt/mssql/bin/sqlservr";
	}

}
