package com.kinlhp.moname.addressing.jpa;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class TestContextConfiguration {

	public static void main(final String[] args) {
		SpringApplication.run(TestContextConfiguration.class, args);
	}

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Test context configuration initialized");
	}

}
