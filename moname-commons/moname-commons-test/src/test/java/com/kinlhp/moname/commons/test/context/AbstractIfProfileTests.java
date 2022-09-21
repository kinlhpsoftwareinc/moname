package com.kinlhp.moname.commons.test.context;

import java.util.Set;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kinlhp.moname.commons.test.TestConfiguration;

@ContextConfiguration(classes = { TestConfiguration.class })
@ExtendWith(value = { SpringExtension.class })
abstract class AbstractIfProfileTests {

	static final String HSQLDB = "hsqldb";
	static final String MYSQL = "mysql";
	static final String ORACLE = "oracle";
	static final String SQLSERVER = "sqlserver";
	static final String TEST = "test";

	@Autowired
	private Environment environment;

	abstract String expectedProfile();

	@DisplayName(value = "Profile assertion.")
	@Test
	final void assertProfile() {
		final var activeProfiles = Set.of(environment.getActiveProfiles());
		Assertions.assertAll("profile", () -> Assertions.assertNotNull(activeProfiles),
			() -> MatcherAssert.assertThat(activeProfiles, Matchers.hasItem(expectedProfile())));
	}

}
