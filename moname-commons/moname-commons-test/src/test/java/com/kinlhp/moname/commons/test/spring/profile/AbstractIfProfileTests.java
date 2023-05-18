package com.kinlhp.moname.commons.test.spring.profile;

import java.util.Set;

import jakarta.annotation.Nonnull;

import lombok.NoArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// TODO: Remove Lombok.
@ExtendWith(value = {SpringExtension.class})
@NoArgsConstructor(onConstructor_ = {@Nonnull})
abstract class AbstractIfProfileTests {

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
	@SuppressWarnings(value = {"NotNullFieldNotInitialized", "unused"})
	private Environment environment;

	@Nonnull
	abstract String expectedProfile();

	@DisplayName(value = "Profile assertion.")
	@Test
	final void assertProfile() {
		@Nonnull final var activeProfiles = Set.of(environment.getActiveProfiles());
		Assertions.assertAll("profile",
			() -> Assertions.assertNotNull(activeProfiles),
			() -> MatcherAssert.assertThat(activeProfiles, Matchers.hasItem(expectedProfile()))
		);
	}
}
