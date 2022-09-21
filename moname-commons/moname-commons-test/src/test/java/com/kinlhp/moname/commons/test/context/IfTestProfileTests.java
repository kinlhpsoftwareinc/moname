package com.kinlhp.moname.commons.test.context;

@IfTestProfile
class IfTestProfileTests extends AbstractIfProfileTests {

	@Override
	String expectedProfile() {
		return TEST;
	}

}
