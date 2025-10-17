package com.kinlhp.moname.commons.test.testcontainers.mysql;

import com.github.dockerjava.api.command.CreateContainerCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

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
	static final String SUBPROTOCOL = MYSQL + "ext";

	private static final Logger LOG = LoggerFactory.getLogger(ExtendedMySQLContainer.class);

	private static final String MYSQL_USER = MONAME;

	private static final String SQL_FILE = "initial-setup.sql";

	public ExtendedMySQLContainer(final DockerImageName dockerImageName) {
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

	@Override
	protected String constructUrlParameters(final String startCharacter, final String delimiter,
			final String endCharacter) {
		return JdbcDatabaseUriExtractableContainer.super
				.constructUrlParameters(urlParameters, startCharacter, delimiter, endCharacter)
				.orElse("");
	}

	@Override
	public String getDriverClassName() {
		return "com.mysql.cj.jdbc.Driver";
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
	public String getSubprotocol() {
		return SUBPROTOCOL;
	}

	@Override
	public String getUsername() {
		return MYSQL_USER;
	}

	private ExtendedMySQLContainer<SELF> withAddExposedPort() {
		super.addExposedPort(MYSQL_PORT);
		return super.self();
	}

	@Override
	public void withAutoResolvedTcPatternUriParameter(final Map.Entry<String, String> parameter) {
		JdbcDatabaseUriExtractableContainer.super.withAutoResolvedTcPatternUriParameter(parameter);
	}

	/**
	 * <a href=https://dev.mysql.com/doc/refman/9.2/en/docker-mysql-more-topics.html#docker-additional-init>
	 * Running Additional Initialization Scripts
	 * </a>
	 */
	private ExtendedMySQLContainer<SELF> withCopyFileToContainer() {
		return super.withCopyFileToContainer(
				MountableFile.forClasspathResource("db/%s/%s".formatted(MYSQL, SQL_FILE), 444),
				"/docker-entrypoint-initdb.d/%s".formatted(SQL_FILE)
		);
	}

	private ExtendedMySQLContainer<SELF> withCreateContainerCmdModifier() {
		return super.withCreateContainerCmdModifier(this::withCreateContainerCmdModifier);
	}

	private void withCreateContainerCmdModifier(final CreateContainerCmd createContainerCmd) {
		createContainerCmd.withHostName(MYSQL);
	}

	private ExtendedMySQLContainer<SELF> withEnv() {
		return super.withEnv("MYSQL_DATABASE", super.getDatabaseName())
				.withEnv("MYSQL_INITDB_SKIP_TZINFO", "false")
				.withEnv("MYSQL_PASSWORD", getPassword())
				.withEnv("MYSQL_ROOT_HOST", MYSQL)
				.withEnv("MYSQL_ROOT_PASSWORD", getPassword())
				.withEnv("MYSQL_USER", getUsername());
	}

	private ExtendedMySQLContainer<SELF> withNetwork() {
		return super.withNetwork(SharedNetwork.getSingleton().getNetwork());
	}

	private ExtendedMySQLContainer<SELF> withNetworkAliases() {
		return super.withNetworkAliases(MYSQL);
	}
}
