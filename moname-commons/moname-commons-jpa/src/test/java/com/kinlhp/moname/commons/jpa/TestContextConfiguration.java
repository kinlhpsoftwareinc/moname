package com.kinlhp.moname.commons.jpa;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

@AutoConfigurationPackage
@SpringBootConfiguration
public class TestContextConfiguration {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(TestContextConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Test context configuration initialized");
	}
}
