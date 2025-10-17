package com.kinlhp.moname.commons.test.spring.profile;

import java.util.Set;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
@NoArgsConstructor
abstract class AbstractIfProfileIT {

	@Nonnull
	static final String HSQLDB = "hsqldb";

	@Nonnull
	static final String MYSQL = "mysql";

	@Nonnull
	static final String ORACLE = "oracle";

	@Nonnull
	static final String SQLSERVER = "sqlserver";

	@Nonnull
	static final String TEST = "test";

	@Autowired
	@Nonnull
	private Environment environment;

	@Nonnull
	abstract String expectedProfile();

	@DisplayName("Profile assertion")
	@Test
	final void assertProfile() {
		@Nonnull final var activeProfiles = Set.of(environment.getActiveProfiles());
		Assertions.assertAll("profile",
				() -> Assertions.assertNotNull(activeProfiles),
				() -> MatcherAssert.assertThat(activeProfiles, Matchers.hasItem(expectedProfile()))
		);
	}
}
