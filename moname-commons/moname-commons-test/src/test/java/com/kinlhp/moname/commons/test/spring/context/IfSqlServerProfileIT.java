package com.kinlhp.moname.commons.test.spring.context;

import lombok.NoArgsConstructor;

@IfSqlServerProfile
@NoArgsConstructor
final class IfSqlServerProfileIT extends AbstractIfProfileIT {

	@Override
	String expectedProfile() {
		return SQLSERVER;
	}
}
