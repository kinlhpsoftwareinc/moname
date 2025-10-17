package com.kinlhp.moname.commons.test.spring.jpa.persistenceunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;

import jakarta.annotation.Nonnull;
import java.util.Objects;

@AutoConfiguration
@EnableConfigurationProperties(PersistenceUnitProperties.class)
public class PersistenceUnitNameCustomizerAutoConfiguration { // TODO: To be tested

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(PersistenceUnitNameCustomizerAutoConfiguration.class);

	@Nonnull
	private final PersistenceUnitProperties properties;

	public PersistenceUnitNameCustomizerAutoConfiguration(@Nonnull final PersistenceUnitProperties properties) {
		this.properties = properties;
	}

	@Bean
	@Nonnull
	EntityManagerFactoryBuilderCustomizer persistenceUnitNameCustomizer() {
		return builder -> builder.setPersistenceUnitPostProcessors(this::rename);
	}

	private void rename(@Nonnull final MutablePersistenceUnitInfo persistenceUnitInfo) {
		@Nonnull final var currentName = Objects.requireNonNullElse(persistenceUnitInfo.getPersistenceUnitName(), "");
		@Nonnull final var proposedNewName = Objects.requireNonNullElse(properties.name(), currentName);
		if (!proposedNewName.equals(currentName)) {
			LOG.debug("Renaming the persistence unit from {} to {}", currentName, proposedNewName);
			persistenceUnitInfo.setPersistenceUnitName(proposedNewName);
		}
	}
}
