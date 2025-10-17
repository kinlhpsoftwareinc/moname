package com.kinlhp.moname.commons.logging.spring.env;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.core.env.ConfigurableEnvironment;

import jakarta.annotation.Nonnull;

import com.kinlhp.moname.commons.logging.AbstractActiveProfilesIT;

import static org.assertj.core.api.Assertions.assertThat;

class SpringProfilesGroupEnvironmentPostProcessorIT extends AbstractActiveProfilesIT {

	@Autowired
	@Nonnull
	private ConfigurableEnvironment environment;

	@Test
	final void shouldExpandGroupsOfActiveProfiles(@Nonnull final CapturedOutput output) {
		@Nonnull final var expected = new String[]{"test", "profile-group", "related-profile"};

		assertThat(environment.containsProperty("spring.profiles.active")).isTrue();
		assertThat(environment.getProperty("spring.profiles.active", String[].class)).isEqualTo(expected);

		assertThat(environment.getPropertySources().contains("expandedGroupsOfActiveProfiles")).isTrue();
		assertThat(
				environment.getPropertySources()
						.get("expandedGroupsOfActiveProfiles")
						.getProperty("spring.profiles.active")
		).isEqualTo(expected);

		/*
		assertThat(output.getOut())
				.contains("The following 3 profiles are active: \"test\", \"profile-group\", \"related-profile\"");
		 */
	}
}
