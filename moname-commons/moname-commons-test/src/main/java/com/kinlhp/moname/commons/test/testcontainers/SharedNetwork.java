package com.kinlhp.moname.commons.test.testcontainers;

import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.ContainerNetworkSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.Network;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class SharedNetwork {

	@Nonnull
	public static final String KEYCLOAK = "keycloak";

	@Nonnull
	public static final String MONAME = "moname";

	@Nonnull
	public static final String MYSQL = "mysql";

	@Nonnull
	public static final String ORACLE = "oracle";

	@Nonnull
	public static final String SQLSERVER = "sqlserver";

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(SharedNetwork.class);

	@Nullable
	private static volatile SharedNetwork singleton = null;

	@Nullable
	private static volatile Network network = null;

	@Nonnull
	public static SharedNetwork getSingleton() {
		if (singleton == null) {
			synchronized (SharedNetwork.class) {
				if (singleton == null) {
					singleton = new SharedNetwork();
				}
			}
		}
		//noinspection DataFlowIssue
		return singleton;
	}

	private SharedNetwork() {
		if (network == null) {
			synchronized (Network.class) {
				if (network == null) {
					network = createNetwork();
				}
			}
		}
	}

	@Nonnull
	private Network createNetwork() {
		return Network.builder()
				.createNetworkCmdModifier(this::createNetworkCmd)
				.build();
	}

	private void createNetworkCmd(@Nonnull final CreateNetworkCmd createNetworkCmd) {
		@Nonnull final var networkName = "%s-%s".formatted(MONAME, UUID.randomUUID());
		createNetworkCmd.withName(networkName);
	}

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Created a singleton shared Testcontainers network");
	}

	@Nonnull
	public String getNetworkId() {
		return DockerClientFactory.instance()
				.client()
				.inspectNetworkCmd()
				.withNetworkId(getNetwork().getId())
				.exec()
				.getId();
	}

	@Nonnull
	public String getNetworkName() {
		return DockerClientFactory.instance()
				.client()
				.inspectNetworkCmd()
				.withNetworkId(getNetwork().getId())
				.exec()
				.getName();
	}

	@Nonnull
	public Stream<Container> getContainers() {
		//noinspection resource
		return DockerClientFactory.instance()
				.client()
				.listContainersCmd()
				.withShowAll(true)
				.exec()
				.stream()
				.filter(this::isItHere);
	}

	private boolean isItHere(@Nonnull final Container container) {
		return Optional.ofNullable(container.getNetworkSettings())
				.map(ContainerNetworkSettings::getNetworks)
				.map(Map::values)
				.stream()
				.flatMap(Collection::stream)
				.anyMatch(this::isItTheSame);
	}

	private boolean isItTheSame(@Nonnull final ContainerNetwork network) {
		return getSingleton().getNetwork().getId().equals(network.getNetworkID());
	}

	@Nonnull
	public Network getNetwork() {
		//noinspection DataFlowIssue
		return network;
	}
}
