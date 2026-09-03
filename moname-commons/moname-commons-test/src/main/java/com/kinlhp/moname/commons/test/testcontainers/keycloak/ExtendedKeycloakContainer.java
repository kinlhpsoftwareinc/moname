package com.kinlhp.moname.commons.test.testcontainers.keycloak;

import com.github.dockerjava.api.command.CreateContainerCmd;
import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import jakarta.annotation.Nonnull;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import com.kinlhp.moname.commons.test.testcontainers.UriExtractableContainer;

import static jakarta.ws.rs.core.Response.Status.OK;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.KEYCLOAK;
import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MONAME;
import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MYSQL;

/**
 * Extended Keycloak container.
 *
 * @see ExtendableKeycloakContainer ExtendableKeycloakContainer
 */
@SuppressWarnings("java:S119")
public class ExtendedKeycloakContainer<SELF extends ExtendedKeycloakContainer<SELF>>
		extends ExtendableKeycloakContainer<SELF>
		implements UriExtractableContainer<SELF> {

	@Nonnull
	public static final String JWKS_PATH = "/protocol/openid-connect/certs";

	@Nonnull
	@SuppressWarnings("java:S1075")
	public static final String KC_HTTP_RELATIVE_PATH = "/auth";

	@Nonnull
	public static final String REALM_PATH = "/realms/%s".formatted(MONAME);

	@Nonnull
	public static final String REALM_RESOURCE_NAME_CLASSPATH = "oidc/%s/%s-realm.json".formatted(KEYCLOAK, MONAME);

	@Nonnull
	public static final String TOKEN_ENDPOINT_PATH = "/protocol/openid-connect/token";

	static final int KC_HTTPS_PORT = 8443;

	@Nonnull
	static final String KC_HTTP_RELATIVE_PATH_PATTERN = "https://%s:%s%s";

	@Nonnull
	private static final String DEFAULT_IMAGE = "quay.io/%s/%s".formatted(KEYCLOAK, KEYCLOAK);

	@Nonnull
	private static final String DEFAULT_TAG = "26.1.2-optimized";

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(ExtendedKeycloakContainer.class);

	private static final int KC_HTTP_MANAGEMENT_PORT = 9000;

	@Nonnull
	@SuppressWarnings("java:S1075")
	private static final String KC_HTTP_MANAGEMENT_RELATIVE_PATH = "/management";

	@Nonnull
	private static final String HEALTH_CHECK_ENDPOINT_PATH = "%s/health".formatted(KC_HTTP_MANAGEMENT_RELATIVE_PATH);

	@Nonnull
	private URI issuerUri = URI.create("%s%s".formatted(KC_HTTP_RELATIVE_PATH_PATTERN.formatted(KEYCLOAK, KC_HTTPS_PORT, KC_HTTP_RELATIVE_PATH), REALM_PATH));

	@SafeVarargs
	public ExtendedKeycloakContainer(@Nonnull final Consumer<SELF>... reconfigures) {
		this(DockerImageName.parse(DEFAULT_IMAGE).withTag(DEFAULT_TAG), reconfigures);
	}

	@SafeVarargs
	@SuppressWarnings("unused")
	public ExtendedKeycloakContainer(@Nonnull final String fullImageName,
			@Nonnull final Consumer<SELF>... reconfigures) {
		this(DockerImageName.parse(fullImageName), reconfigures);
	}

	private ExtendedKeycloakContainer(@Nonnull final DockerImageName dockerImageName,
			@Nonnull final Consumer<SELF>[] reconfigures) {
		super(dockerImageName.asCanonicalNameString());
		Arrays.stream(reconfigures)
				.filter(Objects::nonNull)
				.forEach(reconfigure -> reconfigure.accept(self()));
		LOG.info("Extended Keycloak container");
	}

	@Override
	@SuppressWarnings("java:S125")
	protected void configure() {
		// TODO: https://www.docker.com/blog/testcontainers-best-practices
		/*setPortBindings().*/
		//noinspection resource
		waitingFor()
				.withAddExposedPorts()
				.withCopyFileToContainer()
				.withEnv()
				//.withExposedPorts()
				.withNetworkAliases()
				.withCommand("start", "--import-realm", "--optimized", "--verbose")
				.withCreateContainerCmdModifier(this::withCreateContainerCmdModifier)
				.withAutoResolveUriParameters()
				.withAutoResolveTcPatternUriParameters()
				/*.withNetworkMode()*/;
	}

	@Nonnull
	@Override
	public String getAuthServerUrl() {
		return KC_HTTP_RELATIVE_PATH_PATTERN.formatted(KEYCLOAK, getHttpsPort(), getContextPath());
	}

	@Nonnull
	@Override
	public String getContextPath() {
		return KC_HTTP_RELATIVE_PATH;
	}

	@Override
	public int getHttpsPort() {
		return super.getMappedPort(KC_HTTPS_PORT);
	}

	@Nonnull
	public Optional<URI> getIssuerUri() {
		return Optional.of(issuerUri);
	}

	@Nonnull
	@Override
	public Logger getLogger() {
		return LOG;
	}

	@Nonnull
	@Override
	public Optional<URI> getUri() {
		return getIssuerUri();
	}

	/**
	 * Requires a MySQL instance having {@literal mysql} as hostname on a bridge network named {@literal moname}.
	 *
	 * @see ExtendableKeycloakContainer#start() start
	 */
	@Override
	@SuppressWarnings("java:S112")
	public void start() {
		LOG.warn("Requires a MySQL instance having {} as hostname on the same bridge network", MYSQL);
		super.start();
		try {
			super.execInContainer(commandParts());
		} catch (@Nonnull @SuppressWarnings("java:S2142") final Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * @see ExtendableKeycloakContainer#withCommand(String...) withCommand
	 */
	@Nonnull
	@Override
	public SELF withCommand(@Nonnull final String... commandParts) {
		super.setCommand(commandParts);
		return super.self();
	}

	@Nonnull
	private String[] commandParts() {
		return new String[]{
				"sh",
				"-c",
				"chown --recursive --silent --verbose %s:root /opt/%s/data/import"
						.formatted(KEYCLOAK, KEYCLOAK)
		};
	}

	/*
	@Nonnull
	private ExtendedKeycloakContainer<SEFL> setPortBindings() {
		@Nonnull final var portBindings = List.of(
				"%d:%d".formatted(KC_HTTPS_PORT, KC_HTTPS_PORT),
				"%d:%d".formatted(KC_HTTP_MANAGEMENT_PORT, KC_HTTP_MANAGEMENT_PORT)
		);
		super.setPortBindings(portBindings);
		return self();
	}
	 */

	@Nonnull
	private ExtendedKeycloakContainer<SELF> waitingFor() {
		@Nonnull final var waitStrategy = Wait.forHttps(HEALTH_CHECK_ENDPOINT_PATH)
				.forPort(KC_HTTP_MANAGEMENT_PORT)
				.forStatusCode(OK.getStatusCode())
				// TODO: Caused by: java.lang.RuntimeException: javax.net.ssl.SSLHandshakeException: PKIX path building
				//  failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid
				//  certification path to requested target
				.allowInsecure();
		super.setWaitStrategy(waitStrategy);
		return super.self();
	}

	@Nonnull
	private ExtendedKeycloakContainer<SELF> withAddExposedPorts() {
		super.addExposedPorts(KC_HTTPS_PORT, KC_HTTP_MANAGEMENT_PORT);
		return super.self();
	}

	/**
	 * <a href=https://www.keycloak.org/server/importExport#_importing_a_realm_during_startup>
	 * Importing a Realm during Startup
	 * </a>
	 */
	@Nonnull
	private ExtendedKeycloakContainer<SELF> withCopyFileToContainer() {
		return super.withCopyFileToContainer(
				MountableFile.forClasspathResource(REALM_RESOURCE_NAME_CLASSPATH, 444),
				"/opt/%s/data/import/%s-realm.json".formatted(KEYCLOAK, MONAME)
		);
	}

	private void withCreateContainerCmdModifier(@Nonnull final CreateContainerCmd createContainerCmd) {
		createContainerCmd.withHostName(KEYCLOAK).withUser(KEYCLOAK);
	}

	@Nonnull
	private ExtendedKeycloakContainer<SELF> withEnv() {
		return super.withEnv("KC_BOOTSTRAP_ADMIN_PASSWORD", MONAME)
				.withEnv("KC_BOOTSTRAP_ADMIN_USERNAME", MONAME);
	}

	//@Nonnull
	//private ExtendedKeycloakContainer<SELF> withExposedPorts() {
	//	return super.withExposedPorts(KC_HTTPS_PORT, KC_HTTP_MANAGEMENT_PORT);
	//}

	@Nonnull
	public SELF withIssuerUri(@Nonnull final URI issuerUri) {
		LOG.warn("Replacing the current issuer URI {} with the new issuer URI {}", this.issuerUri, issuerUri);
		this.issuerUri = issuerUri;
		return super.self();
	}

	@Nonnull
	private ExtendedKeycloakContainer<SELF> withNetworkAliases() {
		return super.withNetworkAliases(KEYCLOAK);
	}

	/*
	@Nonnull
	private ExtendedKeycloakContainer<SELF> withNetworkMode() {
		return super.withNetworkMode(MONAME);
	}
	 */

	@Nonnull
	@Override
	public SELF withUri(@Nonnull final URI uri) {
		return withIssuerUri(uri);
	}
}
