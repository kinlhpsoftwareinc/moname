package com.kinlhp.moname.commons.test.spring.jpa;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Abstraction for Data JPA integration test slice.
 * <p>
 * <a href="https://docs.spring.io/spring-boot/appendix/test-auto-configuration/slices.html">
 * Test Slices / {@link DataJpaTest @DataJpaTest}
 * </a>
 */
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public abstract class AbstractDataJpaTestSliceIT {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(AbstractDataJpaTestSliceIT.class);

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Data JPA test slice configuration initialized");
	}
}
