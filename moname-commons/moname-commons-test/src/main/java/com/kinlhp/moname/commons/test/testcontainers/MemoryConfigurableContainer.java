package com.kinlhp.moname.commons.test.testcontainers;

import org.springframework.util.unit.DataSize;
import org.testcontainers.containers.GenericContainer;

import jakarta.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

// TODO: Tests to be implemented
/**
 * @see <a href="https://docs.docker.com/engine/containers/resource_constraints/">Resource constraints</a>
 * @see <a href="https://docs.docker.com/engine/containers/run/#runtime-constraints-on-resources">
 * Runtime constraints on resources
 * </a>
 * @see <a href="https://fabiokung.com/2014/03/13/memory-inside-linux-containers/">Memory inside Linux containers</a>
 */
@SuppressWarnings("java:S119")
public interface MemoryConfigurableContainer<SELF extends GenericContainer<SELF> & MemoryConfigurableContainer<SELF>> {

	/**
	 * The maximum amount of memory the container can use. If you set this option, the minimum allowed value is
	 * {@code 6m} (6 megabytes). That is, you must set the value to at least 6 megabytes.
	 */
	@Nonnull
	SELF withMemory(@Nonnull final Consumer<DataSize> consumer);

	/**
	 * The amount of memory this container is allowed to swap to disk.
	 *
	 * @see <a href="https://docs.docker.com/engine/containers/resource_constraints/#--memory-swap-details">
	 * --memory-swap details
	 * </a>
	 */
	@Nonnull
	SELF withMemorySwap(@Nonnull final Consumer<DataSize> consumer);

	/**
	 * By default, the host kernel can swap out a percentage of anonymous pages used by a container. You can set
	 * {@code --memory-swappiness} to a value between {@code 0} and {@code 100}, to tune this percentage.
	 *
	 * @see <a href="https://docs.docker.com/engine/containers/resource_constraints/#--memory-swappiness-details">
	 * --memory-swappiness details
	 * </a>
	 * @see <a href="https://docs.docker.com/engine/containers/run/#swappiness-constraint">Swappiness constraint</a>
	 */
	@Nonnull
	SELF withMemorySwappiness(@Nonnull final LongConsumer consumer);

	/**
	 * Size of {@code /dev/shm}. The format is {@code <number><unit>}. {@code number} must be greater than {@code 0}.
	 * Unit is optional and can be {@code b} (bytes), {@code k} (kilobytes), {@code m} (megabytes), or {@code g}
	 * (gigabytes). If you omit the unit, the system uses bytes. If you omit the size entirely, the system uses
	 * {@code 64m}.
	 */
	@Nonnull
	SELF withShmSize(@Nonnull final Consumer<DataSize> consumer);
}
