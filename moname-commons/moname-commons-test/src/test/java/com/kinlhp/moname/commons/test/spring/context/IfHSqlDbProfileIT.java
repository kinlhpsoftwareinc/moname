package com.kinlhp.moname.commons.test.spring.context;

import lombok.NoArgsConstructor;

@IfHSqlDbProfile
@NoArgsConstructor
final class IfHSqlDbProfileIT extends AbstractIfProfileIT {

	@Override
	String expectedProfile() {
		return HSQLDB;
	}
}
