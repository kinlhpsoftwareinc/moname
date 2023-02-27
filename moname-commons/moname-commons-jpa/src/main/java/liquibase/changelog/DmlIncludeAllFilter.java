package liquibase.changelog;

import java.util.Locale;

/**
 * Filter to not include data load .csv files as change log files.
 */
public class DmlIncludeAllFilter implements IncludeAllFilter {

	@Override
	public boolean include(final String changeLogPath) {
		return changeLogPath != null
			&& !changeLogPath.toLowerCase(Locale.ROOT).endsWith(".csv");
	}

}
