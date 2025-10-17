package com.kinlhp.moname.commons.jpa.spring.env;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class JpaDefaultsPropertySourceEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

	private final Log log;

	private static final String FILE_BASE_PATH = "com/kinlhp/moname/commons/jpa";

	private static final String FILE_EXTENSION = ".yaml";

	private static final String NON_PROFILED_FILE_NAME = "defaults";

	private static final String NON_PROFILED_FILE_PATH = "%s/%s%s"
			.formatted(FILE_BASE_PATH, NON_PROFILED_FILE_NAME, FILE_EXTENSION);

	private static final String ON_PROFILE_PROPERTY_NAME = "spring.config.activate.on-profile";

	private static final String PROFILED_FILE_PATH_TEMPLATE = "%s/%s-%s%s"
			.formatted(FILE_BASE_PATH, NON_PROFILED_FILE_NAME, "%s", FILE_EXTENSION);

	public JpaDefaultsPropertySourceEnvironmentPostProcessor(final DeferredLogFactory logFactory) {
		log = logFactory.getLog(getClass());
		log.debug("JPA defaults property source environment post processor initialized");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public void postProcessEnvironment(final ConfigurableEnvironment environment, final SpringApplication application) {
		Optional.of(new ClassPathResource(NON_PROFILED_FILE_PATH))
				.filter(ClassPathResource::exists)
				.ifPresentOrElse(
						nonProfiledResource -> processFromNonProfiledResource(environment, nonProfiledResource),
						() -> {
							final var message = """
									The JPA defaults property source file '%s' was not found in the classpath"""
									.formatted(NON_PROFILED_FILE_PATH);
							log.error(message);
							throw new IllegalStateException(message);
						}
				);
	}

	private void processFromNonProfiledResource(final ConfigurableEnvironment environment,
			final ClassPathResource nonProfiledResource) {
		log.trace("The JPA defaults property source file '%s' was found in the classpath"
				.formatted(NON_PROFILED_FILE_PATH));
		sortResourcePriority(environment, nonProfiledResource)
				.stream()
				.map(resource -> loadResource(environment, resource))
				.forEach(activePropertySources -> addActivePropertySources(environment, activePropertySources));
	}

	/**
	 * Sort from <strong>HIGH</strong> to <strong>LOW</strong> precedence: <strong>LAST profile</strong> >
	 * <strong>...</strong> > <strong>FIRST profile</strong> > <strong>non-profiled</strong>.
	 */
	private List<ClassPathResource> sortResourcePriority(final ConfigurableEnvironment environment,
			final ClassPathResource nonProfiledResource) {
		final var sortedResourcePriority = Stream.concat(sortProfiledResourcePriority(environment),
				Stream.of(nonProfiledResource))
				.toList();
		log.debug("The JPA defaults property source priority has been sorted from HIGH to LOW precedence: %s"
				.formatted(sortedResourcePriority));
		return sortedResourcePriority;
	}

	/**
	 * Sorts the resource priority based on the profiles in the environment.
	 * <p>
	 * Sort from <strong>HIGH</strong> to <strong>LOW</strong> precedence: <strong>LAST profile</strong> >
	 * <strong>...</strong> > <strong>FIRST profile</strong>.
	 */
	private Stream<ClassPathResource> sortProfiledResourcePriority(final ConfigurableEnvironment environment) {
		log.trace("""
				The JPA defaults property source priority is being sorted from HIGH to LOW precedence based on the \
				profiles in the environment""");
		return Optional.of(environment.getActiveProfiles())
				.filter(ArrayUtils::isNotEmpty)
				.map(List::of)
				.orElseGet(() -> List.of(environment.getDefaultProfiles()))
				.reversed()
				.stream()
				.map(profile -> new ClassPathResource(PROFILED_FILE_PATH_TEMPLATE.formatted(profile)))
				.filter(Resource::exists);
	}

	private List<PropertySource<?>> loadResource(final ConfigurableEnvironment environment,
			final ClassPathResource resource) {
		log.trace("Loading the JPA defaults property source file '%s' from the classpath"
				.formatted(resource.getPath()));
		try {
			final var loadedPropertySources = new YamlPropertySourceLoader().load(resource.getFilename(), resource)
					.stream()
					.filter(propertySource -> accept(environment, propertySource))
					.toList();
			log.debug("Loaded JPA defaults property sources: %s"
					.formatted(loadedPropertySources.stream().map(PropertySource::getName).toList()));
			return loadedPropertySources;
		} catch (final IOException exception) {
			final var message = "Failed to load the JPA defaults property source file '%s'"
					.formatted(resource.getPath());
			log.error(message);
			throw new UncheckedIOException(message, exception);
		}
	}

	/**
	 * If the property source has not the property {@code spring.config.activate.on-profile}, it is considered active.
	 * Otherwise, check against the environment's active profiles.
	 */
	private boolean accept(final Environment environment, final PropertySource<?> propertySource) {
		final boolean accepted = new Binder(ConfigurationPropertySources.from(propertySource))
				.bind(ON_PROFILE_PROPERTY_NAME, String[].class)
				.map(boundProfiles -> ArrayUtils.isEmpty(boundProfiles)
						|| environment.acceptsProfiles(Profiles.of(boundProfiles)))
				.orElse(true);
		if (!accepted) {
			log.warn("""
					The JPA defaults property source '%s' is not active because it does not match the environment's \
					activation condition"""
					.formatted(propertySource.getName()));
		}
		return accepted;
	}

	/**
	 * Adds the active property sources to the environment.
	 * <p>
	 * <strong>Within a file, the last document has the highest precedence</strong>.
	 */
	private void addActivePropertySources(final ConfigurableEnvironment environment,
			final List<PropertySource<?>> activePropertySources) {
		activePropertySources.reversed()
				.forEach(activePropertySource -> {
					log.trace("Adding the JPA defaults property source '%s' to the environment"
							.formatted(activePropertySource.getName()));
					environment.getPropertySources().addLast(activePropertySource);
				});
	}
}
