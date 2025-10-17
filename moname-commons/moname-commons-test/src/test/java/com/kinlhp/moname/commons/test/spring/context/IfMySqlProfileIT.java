package com.kinlhp.moname.commons.test.spring.context;

import lombok.NoArgsConstructor;

@IfMySqlProfile
@NoArgsConstructor
final class IfMySqlProfileIT extends AbstractIfProfileIT {

	@Override
	String expectedProfile() {
		return MYSQL;
	}
}
