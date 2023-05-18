package com.kinlhp.moname.commons.jpa;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

@AutoConfigurationPackage
@Slf4j
@SpringBootConfiguration
public class TestContextConfiguration {

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Test context configuration initialized");
	}
}
