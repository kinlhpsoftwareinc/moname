package com.kinlhp.moname.commons.test.spring.jpa;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Abstraction for JPA-only integration tests.
 */
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public abstract class AbstractJpaOnlyIT {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(AbstractJpaOnlyIT.class);

	@PostConstruct
	public void postConstruct() {
		LOG.debug("JPA-only test context configuration initialized");
	}
}
