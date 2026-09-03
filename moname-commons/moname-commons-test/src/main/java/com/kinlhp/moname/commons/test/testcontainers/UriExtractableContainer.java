package com.kinlhp.moname.commons.test.testcontainers;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.HostConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.util.unit.DataSize;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.jdbc.ConnectionUrl.Patterns;

import jakarta.annotation.Nonnull;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kinlhp.moname.commons.stream.SingletonCollector;

import static org.springframework.util.unit.DataUnit.BYTES;

// TODO: Tests to be implemented
@SuppressWarnings("java:S119")
public interface UriExtractableContainer<SELF extends GenericContainer<SELF> & UriExtractableContainer<SELF>> {

	/**
	 * @see Patterns#TC_PARAM_NAME_PATTERN TC_PARAM_NAME_PATTERN
	 */
	@Nonnull
	Pattern TC_PARAM_NAME_PATTERN = Pattern.compile("^TC_[A-Z].*");

	/**
	 * @see Patterns#TC_PARAM_NAME_PATTERN TC_PARAM_NAME_PATTERN
	 */
	@Nonnull
	Pattern NOT_TC_PARAM_NAME_PATTERN = Pattern.compile("^(?!TC_[A-Z]).*");

	@Nonnull
	Logger getLogger();

	@Nonnull
	Optional<URI> getUri();

	default void withAutoResolvedUriParameter(@Nonnull final Entry<String, String> parameter) {
		throw new UnsupportedOperationException(
				"It is unclear what to do with the URI parameter %s here".formatted(parameter));
	}

	default void withAutoResolvedTcPatternUriParameter(@Nonnull final Entry<String, String> parameter) {
		//noinspection unchecked
		@Nonnull final var self = (SELF) this;
		Memory.of(parameter.getKey())
				.ifPresent(memory -> memory.type.accept(self, DataSize.parse(parameter.getValue())));
		Behavior.of(parameter.getKey())
				.ifPresent(behavior -> behavior.parameter.accept(self, Boolean.parseBoolean(parameter.getValue())));
	}

	@Nonnull
	default Optional<String> constructUrlParameters(@Nonnull final Map<String, String> urlParameters,
			@Nonnull final String startCharacter, @Nonnull final String delimiter, @Nonnull final String endCharacter) {
		return urlParameters.isEmpty()
				? Optional.empty()
				: Optional.of(sortParams(urlParameters, startCharacter, delimiter, endCharacter));
	}

	@Nonnull
	private String sortParams(@Nonnull final Map<String, String> urlParameters, @Nonnull final String startCharacter,
			@Nonnull final String delimiter, @Nonnull final String endCharacter) {
		getLogger().trace("Sorting URI parameters");
		@Nonnull final var sortedUrlParameters = urlParameters.entrySet().stream()
				.map(Object::toString)
				.sorted()
				.collect(Collectors.joining(delimiter));
		return "%s%s%s".formatted(startCharacter, sortedUrlParameters, endCharacter);
	}

	/**
	 * Only the value for the first occurrence of the parameter will be returned. Consecutive parameters with the same
	 * name will be ignored, regardless of their values.
	 *
	 * @param uri       URI to look for.
	 * @param paramName parameter name to retrieve the value on the first occurrence in the URI parameters.
	 * @return the value on the first occurrence of the parameter in the URI parameters.
	 */
	@Nonnull
	default Optional<String> getParamValue(@Nonnull final URI uri, @Nonnull final String paramName) {
		getLogger().trace("Retrieving the value from the first occurrence of the parameter {} at {}", paramName, uri);
		return getParamsValues(uri, paramName)
				.computeIfAbsent(paramName, ignored -> Collections.emptySet())
				.stream()
				.findFirst();
	}

	/**
	 * Returns all values for all occurrences of the parameters, mapped by parameter.
	 *
	 * @param uri        URI to look for.
	 * @param paramNames parameter names to retrieve the values for all occurrences.
	 * @return returns all values for all occurrences of each parameter provided, mapped by parameter.
	 */
	@Nonnull
	default <T extends Collection<String>> Map<String, T> getParamsValues(@Nonnull final URI uri,
			@Nonnull final String... paramNames) {
		@Nonnull final var filteredParamNames = Arrays.stream(paramNames)
				.filter(paramName -> !paramName.isBlank())
				.toList();
		if (filteredParamNames.isEmpty()) {
			return Collections.emptyMap();
		}
		return getParamsValues(uri, filteredParamNames);
	}

	@Nonnull
	private <T extends Collection<String>> Map<String, T> getParamsValues(@Nonnull final URI uri,
			@Nonnull final Collection<String> paramNames) {
		getLogger().trace("Retrieving all values for all occurrences of parameters {} at {}", paramNames, uri);
		//noinspection unchecked
		return (Map<String, T>) splitInPairs(uri)
				.filter(pair -> paramNames.contains(pair.getKey()))
				.collect(Collectors.groupingBy(
						Entry::getKey,
						Collectors.mapping(Entry::getValue, Collectors.toList())
				));
	}

	/**
	 * Returns all values for all occurrences of the parameters, mapped by parameter.
	 *
	 * @param uri   URI to look for.
	 * @param regex regular expression of parameter names to retrieve the values for all occurrences.
	 * @return returns all values for all occurrences of each parameter that matches the provided regular expression,
	 * mapped by parameter.
	 */
	@Nonnull
	default <T extends Collection<String>> Map<String, T> getMatchingParamsValues(@Nonnull final URI uri,
			@Nonnull final Pattern regex) {
		getLogger().trace("Retrieving all values for all occurrences of parameters that matches {} at {}", regex, uri);
		//noinspection unchecked
		return (Map<String, T>) splitInPairs(uri)
				.filter(pair -> regex.matcher(pair.getKey()).matches())
				.collect(Collectors.groupingBy(
						Entry::getKey,
						Collectors.mapping(Entry::getValue, Collectors.toList())
				));
	}

	@Nonnull
	private Stream<Entry<String, String>> splitInPairs(@Nonnull final URI uri) {
		return Optional.of(uri)
				.map(URI::toString)
				.filter(fullUri -> fullUri.contains(getQueryParamStarter()))
				.map(fullUri -> fullUri.split("\\%s".formatted(getQueryParamStarter()), 2))
				.filter(parts -> parts.length > 1)
				.map(parts -> parts[1])
				.map(params -> params.split(getQueryParamSeparator()))
				.stream()
				.flatMap(Arrays::stream)
				.map(param -> param.split("=", 2))
				.map(pair -> Map.entry(pair[0], pair.length > 1 ? pair[1] : ""));
	}

	@Nonnull
	default String getQueryParamSeparator() {
		return "&";
	}

	@Nonnull
	default String getQueryParamStarter() {
		return "?";
	}

	@Nonnull
	default SELF withAutoResolveUriParameters() {
		getUri().map(uri -> getMatchingParamsValues(uri, NOT_TC_PARAM_NAME_PATTERN))
				.map(Map::entrySet)
				.stream()
				.flatMap(Collection::stream)
				.map(this::explode)
				.flatMap(Collection::stream)
				.forEach(this::withAutoResolvedUriParameter);
		//noinspection unchecked
		return (SELF) this;
	}

	@Nonnull
	default SELF withAutoResolveTcPatternUriParameters() {
		getUri().map(uri -> getMatchingParamsValues(uri, TC_PARAM_NAME_PATTERN))
				.map(Map::entrySet)
				.stream()
				.flatMap(Collection::stream)
				.map(this::explode)
				.flatMap(Collection::stream)
				.forEach(this::withAutoResolvedTcPatternUriParameter);
		//noinspection unchecked
		return (SELF) this;
	}

	@Nonnull
	private Collection<Entry<String, String>> explode(@Nonnull final Entry<String, Collection<String>> pair) {
		return pair.getValue()
				.stream()
				.map(value -> Map.entry(pair.getKey(), value))
				.toList();
	}

	@Nonnull
	default SELF withUri(@Nonnull final URI uri) {
		throw new UnsupportedOperationException("It is unclear what to do with the URI %s here".formatted(uri));
	}

	enum Behavior {

		TC_REUSABLE((container, reusable) -> container.withReuse((Boolean) reusable));

		@Nonnull
		private final BiConsumer<GenericContainer<?>, Object> parameter;

		Behavior(@Nonnull final BiConsumer<GenericContainer<?>, Object> parameter) {
			this.parameter = parameter;
		}

		@Nonnull
		private static Optional<Behavior> of(@Nonnull final String parameter) {
			return Arrays.stream(values())
					.filter(behavior -> behavior.name().equals(parameter))
					.collect(SingletonCollector.toSingle());
		}
	}

	/**
	 * @see <a href="https://docs.docker.com/engine/containers/resource_constraints/">Resource constraints</a>
	 * @see <a href="https://docs.docker.com/engine/containers/run/#runtime-constraints-on-resources">
	 * Runtime constraints on resources
	 * </a>
	 * @see <a href="https://fabiokung.com/2014/03/13/memory-inside-linux-containers/">
	 * Memory inside Linux containers
	 * </a>
	 */
	@SuppressWarnings({"java:S1117"})
	// TODO: Tests to be implemented
	enum Memory {

		TC_MEMORY(Memory::withMemory),
		TC_MEMORY_SWAP(Memory::withMemorySwap),
		TC_MEMORY_SWAPPINESS(Memory::withMemorySwappiness),
		TC_SHM_SIZE(Memory::withShmSize);

		@Nonnull
		private static final Logger LOG = LoggerFactory.getLogger(Memory.class);

		@Nonnull
		private final BiConsumer<GenericContainer<?>, DataSize> type;

		Memory(@Nonnull final BiConsumer<GenericContainer<?>, DataSize> type) {
			this.type = type;
		}

		@Nonnull
		private static Optional<Memory> of(@Nonnull final String type) {
			return Arrays.stream(values())
					.filter(memory -> memory.name().equals(type))
					.collect(SingletonCollector.toSingle());
		}

		/**
		 * The maximum amount of memory the container can use. If you set this option, the minimum allowed value is
		 * {@code 6m} (6 megabytes). That is, you must set the value to at least 6 megabytes.
		 */
		private static void withMemory(@Nonnull final GenericContainer<?> container,
				@DataSizeUnit(BYTES) @Nonnull DataSize memory) {
			container.withCreateContainerCmdModifier(
					createContainerCmd -> hostConfigOf(createContainerCmd).ifPresent(
							hostConfig -> {
								LOG.debug("Setting `--memory=\"{}\"` runtime constraint on resources of {} container",
										memory, container.getDockerImageName());
								hostConfig.withMemory(memory.toBytes());
							}
					)
			);
		}

		/**
		 * The amount of memory this container is allowed to swap to disk.
		 *
		 * @see <a href="https://docs.docker.com/engine/containers/resource_constraints/#--memory-swap-details">
		 * --memory-swap details
		 * </a>
		 */
		private static void withMemorySwap(@Nonnull final GenericContainer<?> container,
				@DataSizeUnit(BYTES) @Nonnull DataSize memorySwap) {
			container.withCreateContainerCmdModifier(
					createContainerCmd -> hostConfigOf(createContainerCmd).ifPresent(
							hostConfig -> {
								LOG.debug("Setting `--memory-swap=\"{}\"` runtime constraint on resources of {} container",
										memorySwap, container.getDockerImageName());
								hostConfig.withMemorySwap(memorySwap.toBytes());
							}
					)
			);
		}

		/**
		 * By default, the host kernel can swap out a percentage of anonymous pages used by a container. You can set
		 * {@code --memory-swappiness} to a value between {@code 0} and {@code 100}, to tune this percentage.
		 *
		 * @see <a href="https://docs.docker.com/engine/containers/resource_constraints/#--memory-swappiness-details">
		 * --memory-swappiness details
		 * </a>
		 * @see <a href="https://docs.docker.com/engine/containers/run/#swappiness-constraint">Swappiness constraint</a>
		 */
		private static void withMemorySwappiness(@Nonnull final GenericContainer<?> container,
				@DataSizeUnit(BYTES) @Nonnull DataSize memorySwappiness) {
			container.withCreateContainerCmdModifier(
					createContainerCmd -> hostConfigOf(createContainerCmd).ifPresent(
							hostConfig -> {
								LOG.debug("Setting `--memory-swappiness=\"{}\"` runtime constraint on resources of {} container",
										memorySwappiness, container.getDockerImageName());
								hostConfig.withMemorySwappiness(memorySwappiness.toBytes());
							}
					)
			);
		}

		/**
		 * Size of {@code /dev/shm}. The format is {@code <number><unit>}. {@code number} must be greater than {@code 0}.
		 * Unit is optional and can be {@code b} (bytes), {@code k} (kilobytes), {@code m} (megabytes), or {@code g}
		 * (gigabytes). If you omit the unit, the system uses bytes. If you omit the size entirely, the system uses
		 * {@code 64m}.
		 */
		private static void withShmSize(@Nonnull final GenericContainer<?> container,
				@DataSizeUnit(BYTES) @Nonnull DataSize shmSize) {
			container.withCreateContainerCmdModifier(
					createContainerCmd -> hostConfigOf(createContainerCmd).ifPresent(
							hostConfig -> {
								LOG.debug("Setting `--shm-size=\"{}\"` runtime constraint on resources of {} container",
										shmSize, container.getDockerImageName());
								hostConfig.withShmSize(shmSize.toBytes());
							}
					)
			);
		}

		@Nonnull
		private static Optional<HostConfig> hostConfigOf(@Nonnull final CreateContainerCmd createContainerCmd) {
			return Optional.ofNullable(createContainerCmd.getHostConfig())
					.or(() -> {
						LOG.warn("""
								HostConfig is still null. Therefore, it will not be possible to reconfigure the {} \
								container's memory based on the connection URI""", createContainerCmd.getImage());
						return Optional.empty();
					});
		}
	}
}
