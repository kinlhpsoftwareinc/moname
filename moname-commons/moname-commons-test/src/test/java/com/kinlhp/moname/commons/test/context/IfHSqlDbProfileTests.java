package com.kinlhp.moname.commons.test.context;

@IfHSqlDbProfile
class IfHSqlDbProfileTests extends AbstractIfProfileTests {

	@Override
	String expectedProfile() {
		return HSQLDB;
	}

}
