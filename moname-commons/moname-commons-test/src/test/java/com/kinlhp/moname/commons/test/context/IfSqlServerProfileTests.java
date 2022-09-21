package com.kinlhp.moname.commons.test.context;

@IfSqlServerProfile
class IfSqlServerProfileTests extends AbstractIfProfileTests {

	@Override
	String expectedProfile() {
		return SQLSERVER;
	}

}
