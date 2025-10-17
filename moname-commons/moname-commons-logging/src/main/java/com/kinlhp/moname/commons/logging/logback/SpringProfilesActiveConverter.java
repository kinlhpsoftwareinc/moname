package com.kinlhp.moname.commons.logging.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import java.util.Optional;

import static java.util.function.Predicate.not;

public class SpringProfilesActiveConverter extends ClassicConverter {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(SpringProfilesActiveConverter.class);

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Spring profiles active (spring.profiles.active) converter initialized");
	}

	@Nonnull
	@Override
	public String convert(@Nonnull final ILoggingEvent event) {
		@Nonnull final var activeProfiles = Optional.ofNullable(getContext().getProperty("ACTIVE_PROFILES"))
				.map(String::strip)
				.filter(not(String::isEmpty))
				.orElse("");
		return "active-profiles=%s".formatted(activeProfiles);
	}
}
