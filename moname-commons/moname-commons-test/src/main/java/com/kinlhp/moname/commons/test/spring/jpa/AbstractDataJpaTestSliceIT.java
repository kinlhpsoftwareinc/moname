package com.kinlhp.moname.commons.test.spring.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.annotation.PostConstruct;

import com.kinlhp.moname.commons.test.spring.jpa.autoconfigure.persistenceunit.PersistenceUnitNameCustomizerAutoConfiguration;

/**
 * Abstraction for Data JPA integration test slice.
 * <p>
 * <a href="https://docs.spring.io/spring-boot/appendix/test-auto-configuration/slices.html">
 * Test Slices / {@link DataJpaTest @DataJpaTest}
 * </a>
 */
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@ImportAutoConfiguration(PersistenceUnitNameCustomizerAutoConfiguration.class)
public abstract class AbstractDataJpaTestSliceIT {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDataJpaTestSliceIT.class);

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Data JPA test slice configuration initialized");
	}
}
