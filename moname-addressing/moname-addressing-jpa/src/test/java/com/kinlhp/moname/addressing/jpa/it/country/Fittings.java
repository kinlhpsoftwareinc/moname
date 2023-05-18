package com.kinlhp.moname.addressing.jpa.it.country;

import com.kinlhp.moname.addressing.jpa.country.Entity;

/**
 * Fittings for the country tests.
 */
final class Fittings {

	static Entity country() {
		return Entity.builder()
			.alpha2Code("BR")
			.alpha3Code("BRA")
			.englishName("Brazil")
			.frenchName("Brésil (le)")
			.internetCctld(".br")
			.pk(76)
			.portugueseName("Brasil")
			.build();
	}

}
