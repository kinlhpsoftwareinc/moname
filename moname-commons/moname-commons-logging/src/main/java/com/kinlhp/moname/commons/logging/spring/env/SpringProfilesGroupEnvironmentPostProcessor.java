package com.kinlhp.moname.commons.logging.spring.env;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

import jakarta.annotation.Nonnull;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class SpringProfilesGroupEnvironmentPostProcessor implements EnvironmentPostProcessor {

	public SpringProfilesGroupEnvironmentPostProcessor(@Nonnull final DeferredLogFactory logFactory) {
		logFactory.getLog(getClass())
				.debug("Spring profiles group (spring.profiles.group) environment post processor initialized");
	}

	@Override
	public void postProcessEnvironment(@Nonnull final ConfigurableEnvironment environment,
			@Nonnull final SpringApplication application) {
		environment.getPropertySources()
				.addFirst(new MapPropertySource(
						"expandedGroupsOfActiveProfiles",
						Map.of("spring.profiles.active", expandedGroupsOfActiveProfiles(environment))
				));
	}

	@Nonnull
	private String[] expandedGroupsOfActiveProfiles(@Nonnull final Environment environment) {
		return Stream.of(environment.getActiveProfiles())
				.map(String::strip)
				.filter(not(String::isEmpty))
				.distinct()
				.toArray(String[]::new);
	}
}
