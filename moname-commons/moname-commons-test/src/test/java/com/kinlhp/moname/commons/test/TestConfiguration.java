package com.kinlhp.moname.commons.test;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;

@Slf4j
@SpringBootConfiguration
public class TestConfiguration {

	@PostConstruct
	public void postConstruct() {
		LOG.info("Test context configuration initialized");
	}

}
