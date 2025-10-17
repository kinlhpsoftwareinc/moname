//package com.kinlhp.moname.commons.test.testcontainers;
//
//import com.github.dockerjava.api.command.CreateContainerCmd;
//import com.github.dockerjava.api.model.HostConfig;
//import org.springframework.boot.convert.DataSizeUnit;
//import org.springframework.util.unit.DataSize;
//import org.testcontainers.containers.GenericContainer;
//
//import jakarta.annotation.Nonnull;
//import java.net.URI;
//import java.util.Optional;
//import java.util.function.Consumer;
//import java.util.function.LongConsumer;
//
//import static org.springframework.util.unit.DataUnit.BYTES;
//
//import static com.kinlhp.moname.commons.test.testcontainers.UriExtractableContainer.Memory.TC_MEMORY;
//import static com.kinlhp.moname.commons.test.testcontainers.UriExtractableContainer.Memory.TC_MEMORY_SWAP;
//import static com.kinlhp.moname.commons.test.testcontainers.UriExtractableContainer.Memory.TC_MEMORY_SWAPPINESS;
//import static com.kinlhp.moname.commons.test.testcontainers.UriExtractableContainer.Memory.TC_SHM_SIZE;
//
//// TODO: Tests to be implemented
//@SuppressWarnings("java:S119")
//public interface MemoryConfigurableUriExtractableContainer<SELF extends GenericContainer<SELF>
//		& MemoryConfigurableUriExtractableContainer<SELF>>
//		extends MemoryConfigurableContainer<SELF>, UriExtractableContainer<SELF> {
//
//	@Nonnull
//	@SuppressWarnings("UnusedReturnValue")
//	default SELF withAutoResolvedMemoryUriParameters() {
//		//noinspection unchecked
//		((SELF) this).withCreateContainerCmdModifier(
//				createContainerCmd -> optionalHostConfigOf(createContainerCmd).ifPresentOrElse(
//						this::withAutoResolvedMemoryUriParameters,
//						() -> getLogger().warn("""
//										HostConfig is still null. Therefore, it will not be possible to reconfigure \
//										the {} container's memory based on the connection URI {}""",
//								((SELF) this).getDockerImageName(), getUri())
//				)
//		);
//		//noinspection unchecked
//		return (SELF) this;
//	}
//
//	@Nonnull
//	private Optional<HostConfig> optionalHostConfigOf(@Nonnull final CreateContainerCmd createContainerCmd) {
//		return Optional.ofNullable(createContainerCmd.getHostConfig());
//	}
//
//	@Nonnull
//	@SuppressWarnings("UnusedReturnValue")
//	default SELF withAutoResolvedMemoryUriParameters(@Nonnull final HostConfig hostConfig) {
//		return withMemory(memory -> withMemory(hostConfig, memory))
//				.withMemorySwap(memorySwap -> withMemorySwap(hostConfig, memorySwap))
//				.withMemorySwappiness(memorySwappiness -> withMemorySwappiness(hostConfig, memorySwappiness))
//				.withShmSize(shmSize -> withShmSize(hostConfig, shmSize));
//	}
//
//	@Nonnull
//	@Override
//	default SELF withMemory(@Nonnull final Consumer<DataSize> consumer) {
//		//noinspection unchecked
//		return getUri().map(uri -> withMemory(uri, consumer)).orElse((SELF) this);
//	}
//
//	@Nonnull
//	private SELF withMemory(@Nonnull final URI uri, @Nonnull final Consumer<DataSize> consumer) {
//		getParamValue(uri, TC_MEMORY.name())
//				.map(DataSize::parse)
//				.ifPresent(memory -> withMemory(memory, consumer));
//		//noinspection unchecked
//		return (SELF) this;
//	}
//
//	private void withMemory(@DataSizeUnit(BYTES) @Nonnull final DataSize memory,
//			@Nonnull final Consumer<DataSize> consumer) {
//		//noinspection unchecked
//		getLogger().debug("Setting `--memory=\"{}\"` runtime constraint on resources of {} container",
//				memory, ((SELF) this).getDockerImageName());
//		consumer.accept(memory);
//	}
//
//	@Nonnull
//	@Override
//	default SELF withMemorySwap(@Nonnull final Consumer<DataSize> consumer) {
//		//noinspection unchecked
//		return getUri().map(uri -> withMemorySwap(uri, consumer)).orElse((SELF) this);
//	}
//
//	@Nonnull
//	private SELF withMemorySwap(@Nonnull final URI uri, @Nonnull final Consumer<DataSize> consumer) {
//		getParamValue(uri, TC_MEMORY_SWAP.name())
//				.map(DataSize::parse)
//				.ifPresent(memorySwap -> withMemorySwap(memorySwap, consumer));
//		//noinspection unchecked
//		return (SELF) this;
//	}
//
//	private void withMemorySwap(@DataSizeUnit(BYTES) @Nonnull final DataSize memorySwap,
//			@Nonnull final Consumer<DataSize> consumer) {
//		//noinspection unchecked
//		getLogger().debug("Setting `--memory-swap=\"{}\"` runtime constraint on resources of {} container",
//				memorySwap, ((SELF) this).getDockerImageName());
//		consumer.accept(memorySwap);
//	}
//
//	@Nonnull
//	@Override
//	default SELF withMemorySwappiness(@Nonnull final LongConsumer consumer) {
//		//noinspection unchecked
//		return getUri().map(uri -> withMemorySwappiness(uri, consumer)).orElse((SELF) this);
//	}
//
//	@Nonnull
//	private SELF withMemorySwappiness(@Nonnull final URI uri, @Nonnull final LongConsumer consumer) {
//		getParamValue(uri, TC_MEMORY_SWAPPINESS.name())
//				.map(Long::parseLong)
//				.ifPresent(memorySwappiness -> withMemorySwappiness(memorySwappiness, consumer));
//		//noinspection unchecked
//		return (SELF) this;
//	}
//
//	private void withMemorySwappiness(@Nonnull final Long memorySwappiness, @Nonnull final LongConsumer consumer) {
//		//noinspection unchecked
//		getLogger().debug("Setting `--memory-swappiness=\"{}\"` runtime constraint on resources of {} container",
//				memorySwappiness, ((SELF) this).getDockerImageName());
//		consumer.accept(memorySwappiness);
//	}
//
//	@Nonnull
//	@Override
//	default SELF withShmSize(@Nonnull final Consumer<DataSize> consumer) {
//		//noinspection unchecked
//		return getUri().map(uri -> withShmSize(uri, consumer)).orElse((SELF) this);
//	}
//
//	@Nonnull
//	private SELF withShmSize(@Nonnull final URI uri, @Nonnull final Consumer<DataSize> consumer) {
//		getParamValue(uri, TC_SHM_SIZE.name())
//				.map(DataSize::parse)
//				.ifPresent(shmSize -> withShmSize(shmSize, consumer));
//		//noinspection unchecked
//		return (SELF) this;
//	}
//
//	private void withShmSize(@DataSizeUnit(BYTES) @Nonnull final DataSize shmSize,
//			@Nonnull final Consumer<DataSize> consumer) {
//		//noinspection unchecked
//		getLogger().debug("Setting `--shm-size=\"{}\"` runtime constraint on resources of {} container",
//				shmSize, ((SELF) this).getDockerImageName());
//		consumer.accept(shmSize);
//	}
//
//	@Nonnull
//	@SuppressWarnings("UnusedReturnValue")
//	default SELF withMemory(@Nonnull final HostConfig hostConfig, @DataSizeUnit(BYTES) @Nonnull final DataSize memory) {
//		hostConfig.withMemory(memory.toBytes());
//		//noinspection unchecked
//		return (SELF) this;
//	}
//
//	@Nonnull
//	@SuppressWarnings("UnusedReturnValue")
//	default SELF withMemorySwap(@Nonnull final HostConfig hostConfig,
//			@DataSizeUnit(BYTES) @Nonnull final DataSize memorySwap) {
//		hostConfig.withMemorySwap(memorySwap.toBytes());
//		//noinspection unchecked
//		return (SELF) this;
//	}
//
//	@Nonnull
//	@SuppressWarnings("UnusedReturnValue")
//	default SELF withMemorySwappiness(@Nonnull final HostConfig hostConfig, @Nonnull final Long memorySwappiness) {
//		hostConfig.withMemorySwappiness(memorySwappiness);
//		//noinspection unchecked
//		return (SELF) this;
//	}
//
//	@Nonnull
//	@SuppressWarnings("UnusedReturnValue")
//	default SELF withShmSize(@Nonnull final HostConfig hostConfig,
//			@DataSizeUnit(BYTES) @Nonnull final DataSize shmSize) {
//		hostConfig.withShmSize(shmSize.toBytes());
//		//noinspection unchecked
//		return (SELF) this;
//	}
//}
