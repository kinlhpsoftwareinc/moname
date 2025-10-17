package com.kinlhp.moname.commons.api.spring.configuration.logging;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@AutoConfiguration
public class CommonsRequestLoggingFilterAutoConfiguration {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(CommonsRequestLoggingFilterAutoConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Autoconfiguring request logging filter");
	}

	@Bean
	@Nonnull
	CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
		@Nonnull final var loggingFilter = new CommonsRequestLoggingFilter();
		loggingFilter.setIncludeClientInfo(true);
		loggingFilter.setIncludePayload(true);
		loggingFilter.setIncludeQueryString(true);
		loggingFilter.setIncludeHeaders(false);
		return loggingFilter;
	}
}
