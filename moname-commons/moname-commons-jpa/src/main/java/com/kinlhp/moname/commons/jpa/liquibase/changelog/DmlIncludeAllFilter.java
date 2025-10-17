package com.kinlhp.moname.commons.jpa.liquibase.changelog;

import java.util.Locale;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import liquibase.changelog.IncludeAllFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter to not include data load .csv files as change log files.
 */
@SuppressWarnings("unused")
public class DmlIncludeAllFilter implements IncludeAllFilter {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(DmlIncludeAllFilter.class);

	@Override
	public boolean include(@Nullable final String changeLogPath) {
		LOG.debug("Filtering to not include .csv data load files as change log files");
		return changeLogPath != null
				&& !changeLogPath.toLowerCase(Locale.ROOT).endsWith(".csv");
	}
}
