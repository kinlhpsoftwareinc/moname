package com.kinlhp.moname.commons.test.spring.context;

import lombok.NoArgsConstructor;

@IfOracleProfile
@NoArgsConstructor
final class IfOracleProfileIT extends AbstractIfProfileIT {

	@Override
	String expectedProfile() {
		return ORACLE;
	}
}
