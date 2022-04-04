package com.kinlhp.moname.commons.jpa;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = { "com.kinlhp.moname.commons.jpa.repository" })
@EntityScan(basePackages = { "com.kinlhp.moname.commons.jpa.entity" })
@Profile(value = { "test" })
@Slf4j
@SpringBootConfiguration
public class TestConfiguration {

	@PostConstruct
	public void postConstruct() {
		LOG.info("Test context configuration initialized by \"test\" profile");
	}

}
