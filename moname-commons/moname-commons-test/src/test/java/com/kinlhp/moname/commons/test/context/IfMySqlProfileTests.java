package com.kinlhp.moname.commons.test.context;

@IfMySqlProfile
class IfMySqlProfileTests extends AbstractIfProfileTests {

	@Override
	String expectedProfile() {
		return MYSQL;
	}

}
