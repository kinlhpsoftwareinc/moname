package com.kinlhp.moname.commons.test.context;

@IfOracleProfile
class IfOracleProfileTests extends AbstractIfProfileTests {

	@Override
	String expectedProfile() {
		return ORACLE;
	}

}
