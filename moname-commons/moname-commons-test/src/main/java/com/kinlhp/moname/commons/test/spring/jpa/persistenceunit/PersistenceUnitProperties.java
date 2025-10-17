package com.kinlhp.moname.commons.test.spring.jpa.persistenceunit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import jakarta.annotation.Nonnull;

@ConfigurationProperties("spring.application")
public record PersistenceUnitProperties(@DefaultValue("default") @Nonnull String name) {
}
