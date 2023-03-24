package com.kinlhp.moname.addressing.domain;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

@AutoConfigurationPackage
@Slf4j
@SpringBootConfiguration
public class TestConfiguration {

	@PostConstruct
	public void postConstruct() {
		LOG.info("Test context configuration initialized");
	}

}
