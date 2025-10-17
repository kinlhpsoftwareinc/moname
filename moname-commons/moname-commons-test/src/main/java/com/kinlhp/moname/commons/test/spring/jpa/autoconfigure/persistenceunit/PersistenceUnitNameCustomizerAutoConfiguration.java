package com.kinlhp.moname.commons.test.spring.jpa.autoconfigure.persistenceunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;

import java.util.Objects;

@AutoConfiguration
@EnableConfigurationProperties(PersistenceUnitProperties.class)
public class PersistenceUnitNameCustomizerAutoConfiguration { // TODO: To be tested

	private static final Logger LOG = LoggerFactory.getLogger(PersistenceUnitNameCustomizerAutoConfiguration.class);

	private final PersistenceUnitProperties properties;

	public PersistenceUnitNameCustomizerAutoConfiguration(final PersistenceUnitProperties properties) {
		this.properties = properties;
	}

	@Bean
	EntityManagerFactoryBuilderCustomizer persistenceUnitNameCustomizer() {
		return builder -> builder.setPersistenceUnitPostProcessors(this::rename);
	}

	private void rename(final MutablePersistenceUnitInfo persistenceUnitInfo) {
		final var currentName = Objects.requireNonNullElse(persistenceUnitInfo.getPersistenceUnitName(), "");
		final var proposedNewName = Objects.requireNonNullElse(properties.persistenceUnitName(), currentName);
		if (!proposedNewName.equals(currentName)) {
			LOG.debug("Renaming the persistence unit from {} to {}", currentName, proposedNewName);
			persistenceUnitInfo.setPersistenceUnitName(proposedNewName);
		}
	}
}
