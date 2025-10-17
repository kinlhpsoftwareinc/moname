package com.kinlhp.moname.commons.test.spring.context;

import java.util.Set;

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

	static final String HSQLDB = "hsqldb";

	static final String KEYCLOAK = "keycloak";

	static final String MYSQL = "mysql";

	static final String ORACLE = "oracle";

	static final String SECURITY = "security";

	static final String SQLSERVER = "sqlserver";

	static final String TEST = "test";

	@Autowired
	private Environment environment;

	abstract String expectedProfile();

	@DisplayName("Profile assertion")
	@Test
	final void assertProfile() {
		final var activeProfiles = Set.of(environment.getActiveProfiles());
		Assertions.assertAll("profile",
				() -> Assertions.assertNotNull(activeProfiles),
				() -> MatcherAssert.assertThat(activeProfiles, Matchers.hasItem(expectedProfile()))
		);
	}
}
