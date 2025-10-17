package com.kinlhp.moname.commons.test.testcontainers;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.jdbc.ContainerDatabaseDriver;

import jakarta.annotation.Nonnull;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MONAME;

// TODO: Tests to be implemented
@SuppressWarnings("java:S119")
public interface JdbcDatabaseUriExtractableContainer<SELF extends JdbcDatabaseContainer<SELF>
		& JdbcDatabaseUriExtractableContainer<SELF>>
		extends UriExtractableContainer<SELF> {

	@Nonnull
	String getSubprotocol();

	@Nonnull
	default SELF withDatabaseName() {
		//noinspection unchecked
		return getContainerDatabaseDriverJdbcUri()
				.map(this::getDatabaseName)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(((SELF) this)::withDatabaseName)
				.orElseGet(() -> ((SELF) this).withDatabaseName("%s_test".formatted(MONAME)));
	}

	@Nonnull
	default Optional<URI> getContainerDatabaseDriverJdbcUri() {
		@Nonnull final var subprotocol = "jdbc:tc:%s:".formatted(getSubprotocol());
		getLogger().trace("""
						Attempting to find the JDBC URL for subprotocol "{}" in ContainerDatabaseDriver's JDBC URL \
						container cache""",
				subprotocol);
		return reflectOverContainerDatabaseDriver()
				.map(Map::keySet)
				.stream()
				.flatMap(Collection::stream)
				.filter(jdbcUrl -> jdbcUrl.startsWith(subprotocol))
				.map(URI::create)
				.findFirst();
	}

	@Nonnull
	@SuppressWarnings("java:S3011")
	private <T extends JdbcDatabaseContainer<T>> Optional<Map<String, T>> reflectOverContainerDatabaseDriver() {
		return Optional.of(ContainerDatabaseDriver.class)
				.map(driverClass -> {
					try {
						@Nonnull final var field = driverClass.getDeclaredField("jdbcUrlContainerCache");
						field.setAccessible(true);
						//noinspection unchecked
						@Nonnull final var cache = (Map<String, T>) field.get(null);
						field.setAccessible(false);
						return cache;
					} catch (@Nonnull final ReflectiveOperationException exception) {
						getLogger().warn("Failed to access private field jdbcUrlContainerCache by reflection in {}",
								driverClass.getName());
						return null;
					}
				});
	}

	@Nonnull
	default Optional<String> getDatabaseName(@Nonnull final URI jdbcUrl) {
		getLogger().trace("Extracting the database name from the JDBC URL {}", jdbcUrl);
		@Nonnull final var databaseNamePattern = Pattern.compile("(?<=/)[^/?]+(?=\\?|$)");
		@Nonnull final var databaseName = databaseNamePattern.matcher(jdbcUrl.toString())
				.results()
				.map(MatchResult::group)
				.findFirst();
		databaseName.ifPresentOrElse(
				extractedDatabaseName -> getLogger().debug("The database name {} was extracted from the JDBC URI {}",
						extractedDatabaseName, jdbcUrl),
				() -> getLogger().warn("The database name could not be extracted from the JDBC URL {}", jdbcUrl)
		);
		return databaseName;
	}

	@Nonnull
	@Override
	default Optional<URI> getUri() {
		return getContainerDatabaseDriverJdbcUri();
	}

	//@Override
	//default SELF withUri(@Nonnull final URI uri) {
	//	getLogger().trace(
	//			"There is nothing to be done with the URI {}, as the JDBC URL must be obtained via the getUri() method.",
	//			uri);
	//	//noinspection unchecked
	//	return (SELF) this;
	//}

	@Override
	default void withAutoResolvedUriParameter(@Nonnull final Entry<String, String> parameter) {
		//noinspection unchecked
		((SELF) this).withUrlParam(parameter.getKey(), parameter.getValue());
	}

	/*
	@Override
	default void withAutoResolvedTcPatternUriParameter(@Nonnull final Entry<String, String> parameter) {
		UriExtractableContainer.super.withAutoResolvedTcPatternUriParameter(parameter);

		// TODO: Caused by: java.lang.UnsupportedOperationException
		//  -> org.testcontainers.jdbc.ContainerDatabaseDriver.connect(String url, final Properties info)
		//   \-> `Map<String, String> parameters = connectionUrl.getContainerParameters();`
		//     \-> `container.setParameters(parameters);`
		//noinspection unchecked
		((SELF) this).addParameter(parameter.getKey(), parameter.getValue());
	}
	 */

	//@Nonnull
	//default SELF withUrlParam() { // TODO: Move to UriExtractableContainer
	//	getUri().map(uri -> getMatchingParamsValues(uri, NOT_TC_PARAM_NAME_PATTERN))
	//			.map(Map::entrySet)
	//			.stream()
	//			.flatMap(Collection::stream)
	//			.map(paramValues -> paramValues.getValue()
	//					.stream()
	//					.map(paramValue -> Map.entry(paramValues.getKey(), paramValue))
	//					.toList()
	//			)
	//			.flatMap(Collection::stream)
	//			.forEach(parameter -> {
	//				//withUrlParam(parameter); // TODO: Implement withUrlParam(Entry<String, String> parameter) in UriExtractableContainer
	//			});
	//	//noinspection unchecked
	//	return (SELF) this;
	//}

	//@Nonnull
	//@SuppressWarnings("UnusedReturnValue")
	//default SELF withAutoResolvedMemoryUriParameters() {
	//	//noinspection unchecked
	//	return withAutoResolvedMemoryUriParameters(((SELF) this).getCurrentContainerInfo().getHostConfig());
	//}
	//
	//@Nonnull
	//default SELF withAutoResolvedMemoryUriParameters(@Nonnull final HostConfig hostConfig) {
	//	//noinspection resource
	//	return withMemory(memory -> withMemory(hostConfig, memory))
	//			.withMemorySwap(memorySwap -> withMemorySwap(hostConfig, memorySwap))
	//			.withMemorySwappiness(memorySwappiness -> withMemorySwappiness(hostConfig, memorySwappiness))
	//			.withShmSize(shmSize -> withShmSize(hostConfig, shmSize));
	//}
	//
	//@Nonnull
	//@SuppressWarnings("UnusedReturnValue")
	//default SELF withMemory(@Nonnull final HostConfig hostConfig, @DataSizeUnit(BYTES) @Nonnull final DataSize memory) {
	//	hostConfig.withMemory(memory.toBytes());
	//	//noinspection unchecked
	//	return (SELF) this;
	//}
	//
	//@Nonnull
	//@SuppressWarnings("UnusedReturnValue")
	//default SELF withMemorySwap(@Nonnull final HostConfig hostConfig,
	//		@DataSizeUnit(BYTES) @Nonnull final DataSize memorySwap) {
	//	hostConfig.withMemorySwap(memorySwap.toBytes());
	//	//noinspection unchecked
	//	return (SELF) this;
	//}
	//
	//@Nonnull
	//@SuppressWarnings("UnusedReturnValue")
	//default SELF withMemorySwappiness(@Nonnull final HostConfig hostConfig, @Nonnull final Long memorySwappiness) {
	//	hostConfig.withMemorySwappiness(memorySwappiness);
	//	//noinspection unchecked
	//	return (SELF) this;
	//}
	//
	//@Nonnull
	//@SuppressWarnings("UnusedReturnValue")
	//default SELF withShmSize(@Nonnull final HostConfig hostConfig,
	//		@DataSizeUnit(BYTES) @Nonnull final DataSize shmSize) {
	//	hostConfig.withShmSize(shmSize.toBytes());
	//	//noinspection unchecked
	//	return (SELF) this;
	//}
}
