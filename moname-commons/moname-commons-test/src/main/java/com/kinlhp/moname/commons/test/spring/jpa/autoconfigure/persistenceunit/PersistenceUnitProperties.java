package com.kinlhp.moname.commons.test.spring.jpa.autoconfigure.persistenceunit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.context.properties.bind.Name;

@ConfigurationProperties("spring.application")
public record PersistenceUnitProperties(@DefaultValue("default") @Name("name") String persistenceUnitName) {
}
