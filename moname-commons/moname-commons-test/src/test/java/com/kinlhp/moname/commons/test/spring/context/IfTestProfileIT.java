package com.kinlhp.moname.commons.test.spring.context;

import lombok.NoArgsConstructor;

@IfTestProfile
@NoArgsConstructor
final class IfTestProfileIT extends AbstractIfProfileIT {

	@Override
	String expectedProfile() {
		return TEST;
	}
}
