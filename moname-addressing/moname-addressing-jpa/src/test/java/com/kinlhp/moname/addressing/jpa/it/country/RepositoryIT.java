package com.kinlhp.moname.addressing.jpa.it.country;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.addressing.jpa.country.Entity;
import com.kinlhp.moname.addressing.jpa.country.Repository;
import com.kinlhp.moname.commons.test.spring.jpa.AbstractJpaOnlyIT;

/**
 * Tests for country repository.
 */
class RepositoryIT extends AbstractJpaOnlyIT {

	@Autowired
	private Repository repository;

	@DisplayName(value = "Count.")
	@Test
	final void shouldCount() {
		Assertions.assertThat(repository.count())
			.isEqualTo(249L);
	}

	@DisplayName(value = "Countries.")
	@Test
	final void shouldRetrieve() {
		final var countries = repository.findAll();
		/*
		https://stackoverflow.com/questions/47969970/assertj-for-a-pojo-how-to-check-each-nested-property-field-in-one-chained-sente#comment82935909_47975160
		That won't work because after applying extracting you end up performing assertions on the collection of values
		corresponding to the extracted properties. If you use extracting again it will try to extract properties from
		each element of the previous collection of values.
		 */
		Assertions.assertThat(countries)
			.hasSize(249)
			.first()
			.extracting(
				Entity::getPK,
				Entity::getAlpha2Code,
				Entity::getAlpha3Code,
				Entity::getInternetCctld,
				Entity::getEnglishName,
				Entity::getFrenchName,
				Entity::getPortugueseName
			)
			.doesNotContainNull()
			.containsExactly(4, "AF", "AFG", ".af", "Afghanistan", "Afghanistan (l')", "Afeganistão");
		Assertions.assertThat(countries)
			.element(23)
			.extracting(
				Entity::getPK,
				Entity::getAlpha2Code,
				Entity::getAlpha3Code,
				Entity::getInternetCctld,
				Entity::getEnglishName,
				Entity::getFrenchName,
				Entity::getPortugueseName
			)
			.containsExactly(74, "BV", "BVT", null, "Bouvet Island", "Bouvet (l'Île)", "Ilha Bouvet");
		Assertions.assertThat(countries)
			.last()
			.extracting(
				Entity::getPK,
				Entity::getAlpha2Code,
				Entity::getAlpha3Code,
				Entity::getInternetCctld,
				Entity::getEnglishName,
				Entity::getFrenchName,
				Entity::getPortugueseName
			)
			.doesNotContainNull()
			.containsExactly(894, "ZM", "ZMB", ".zm", "Zambia", "Zambie (la)", "Zâmbia");
	}

	@DisplayName(value = "Projection.")
	@Test
	final void shouldProject() {
		Assertions.assertThat(repository.findById(76).orElseThrow())
			.extracting(
				Entity::getPK,
				Entity::getAlpha2Code,
				Entity::getAlpha3Code,
				Entity::getInternetCctld,
				Entity::getEnglishName,
				Entity::getFrenchName,
				Entity::getPortugueseName
			)
			.doesNotContainNull()
			.containsExactly(76, "BR", "BRA", ".br", "Brazil", "Brésil (le)", "Brasil");
	}
}
