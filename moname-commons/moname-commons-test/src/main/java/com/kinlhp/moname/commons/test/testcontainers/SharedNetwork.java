package com.kinlhp.moname.commons.test.testcontainers;

import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.ContainerNetworkSettings;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.Network;

import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class SharedNetwork {

	public static final String KEYCLOAK = "keycloak";

	public static final String MONAME = "moname";

	public static final String MYSQL = "mysql";

	public static final String ORACLE = "oracle";

	public static final String SQLSERVER = "sqlserver";

	private static final Logger LOG = LoggerFactory.getLogger(SharedNetwork.class);

	/**
	 * Safe private lock-splitting approach.
	 */
	private static final SharedNetworkPrivateLock SHARED_NETWORK_PRIVATE_LOCK = new SharedNetworkPrivateLock();

	/**
	 * Safe private lock-splitting approach.
	 */
	private static final NetworkPrivateLock NETWORK_PRIVATE_LOCK = new NetworkPrivateLock();

	@SuppressWarnings("java:S3077")
	private static volatile @Nullable SharedNetwork singleton = null;

	@SuppressWarnings("java:S3077")
	private static volatile @Nullable Network network = null;

	public static SharedNetwork getSingleton() {
		// The local variable `readingReducer` is not merely cosmetic. It reduces the number of reads of the volatile
		// field from two or three to one on the fast path. It is a micro-optimization, but it comes for free.
		var readingReducer = singleton;
		if (readingReducer == null) {
			synchronized (SHARED_NETWORK_PRIVATE_LOCK) {
				readingReducer = singleton;
				if (readingReducer == null) {
					try {
						readingReducer = new SharedNetwork();
						singleton = readingReducer;
					} catch (final Exception exception) {
						singleton = null;
						throw exception;
					}
				}
			}
		}
		return readingReducer;
	}

	private SharedNetwork() {
		// The local variable `readingReducer` is not merely cosmetic. It reduces the number of reads of the volatile
		// field from two or three to one on the fast path. It is a micro-optimization, but it comes for free.
		var readingReducer = network;
		if (readingReducer == null) {
			synchronized (NETWORK_PRIVATE_LOCK) {
				readingReducer = network;
				if (readingReducer == null) {
					try {
						readingReducer = createNetwork();
						network = readingReducer;
					} catch (final Exception exception) {
						network = null;
						throw exception;
					}
				}
			}
		}
	}

	private Network createNetwork() {
		return Network.builder()
				.createNetworkCmdModifier(this::createNetworkCmd)
				.build();
	}

	private void createNetworkCmd(final CreateNetworkCmd createNetworkCmd) {
		final var networkName = "%s-%s".formatted(MONAME, UUID.randomUUID());
		createNetworkCmd.withName(networkName);
	}

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Created a singleton shared Testcontainers network");
	}

	public String getNetworkId() {
		return DockerClientFactory.instance()
				.client()
				.inspectNetworkCmd()
				.withNetworkId(getNetwork().getId())
				.exec()
				.getId();
	}

	public String getNetworkName() {
		return DockerClientFactory.instance()
				.client()
				.inspectNetworkCmd()
				.withNetworkId(getNetwork().getId())
				.exec()
				.getName();
	}

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

	private boolean isItHere(final Container container) {
		return Optional.ofNullable(container.getNetworkSettings())
				.map(ContainerNetworkSettings::getNetworks)
				.map(Map::values)
				.stream()
				.flatMap(Collection::stream)
				.anyMatch(this::isItTheSame);
	}

	private boolean isItTheSame(final ContainerNetwork network) {
		return getSingleton().getNetwork().getId().equals(network.getNetworkID());
	}

	public Network getNetwork() {
		//noinspection DataFlowIssue
		return network;
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
	private static final class SharedNetworkPrivateLock {
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
	private static final class NetworkPrivateLock {
	}
}
