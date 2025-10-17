package com.kinlhp.moname.commons.logging.logback;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.system.CapturedOutput;

import com.kinlhp.moname.commons.logging.AbstractActiveProfilesIT;

import static org.assertj.core.api.Assertions.assertThat;

class SpringProfilesActiveConverterIT extends AbstractActiveProfilesIT {

	@Test
	final void shouldLogActiveProfilesInLogPattern(final CapturedOutput output) {
		assertThat(output.getOut())
				.doesNotContain("There is no conversion supplier registered for conversion word")
				.doesNotContain("is not a valid conversion word")
				.doesNotContain("_IS_UNDEFINED")
				.contains("[active-profiles=test,profile-group,related-profile]");
	}
}
