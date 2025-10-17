package com.kinlhp.moname.addressing.jpa.it.country;

import jakarta.annotation.Nonnull;
import javax.sql.DataSource;

import org.assertj.db.api.Assertions;
import org.assertj.db.type.AssertDbConnectionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.test.spring.jpa.AbstractDataJpaTestSliceIT;

/**
 * Tests for country entity.
 */
class EntityIT extends AbstractDataJpaTestSliceIT {

	@Autowired
	@Nonnull
	private DataSource dataSource;

	@DisplayName("Count")
	@Test
	final void shouldCount() {
		@Nonnull final var query = """
			SELECT
			  COUNT(numeric_code) AS count
			FROM
			  country""";
		@Nonnull final var requestBuilder = AssertDbConnectionFactory.of(dataSource).create().request(query);
		Assertions.assertThat(requestBuilder.build())
			.hasNumberOfRows(1)
			.hasNumberOfColumns(1)
			.row()
			.value().isEqualTo(249);
	}

	@DisplayName("Countries")
	@Test
	final void shouldRetrieve() {
		@Nonnull final var table = AssertDbConnectionFactory.of(dataSource).create().table("country").build();
		Assertions.assertThat(table)
			.hasNumberOfRows(249)
			.hasNumberOfColumns(7)
			.row()
			.value("numeric_code").isEqualTo(4)
			.value("alpha_2_code").isEqualTo("AF")
			.value("alpha_3_code").isEqualTo("AFG")
			.value("internet_cctld").isEqualTo(".af")
			.value("english_name").isEqualTo("Afghanistan")
			.value("french_name").isEqualTo("Afghanistan (l')")
			.value("portuguese_name").isEqualTo("Afeganistão")
			.returnToRow()
			.returnToTable()
			.row(23)
			.value("numeric_code").isEqualTo(74)
			.value("alpha_2_code").isEqualTo("BV")
			.value("alpha_3_code").isEqualTo("BVT")
			.value("internet_cctld").isNull()
			.value("english_name").isEqualTo("Bouvet Island")
			.value("french_name").isEqualTo("Bouvet (l'Île)")
			.value("portuguese_name").isEqualTo("Ilha Bouvet")
			.returnToRow()
			.returnToTable()
			.row(248)
			.value("numeric_code").isEqualTo(894)
			.value("alpha_2_code").isEqualTo("ZM")
			.value("alpha_3_code").isEqualTo("ZMB")
			.value("internet_cctld").isEqualTo(".zm")
			.value("english_name").isEqualTo("Zambia")
			.value("french_name").isEqualTo("Zambie (la)")
			.value("portuguese_name").isEqualTo("Zâmbia");
	}

	@DisplayName("Get numeric code")
	@Test
	final void shouldGetNumericCode() {
		@Nonnull final var country = Fittings.country();
		org.assertj.core.api.Assertions.assertThat(country.getNumericCode()).isEqualTo("076");
	}

	@DisplayName("Set numeric code")
	@Test
	final void shouldSetNumericCode() {
		@Nonnull final var country = Fittings.country();
		org.junit.jupiter.api.Assertions.assertAll("setNumericCode()",
			() -> org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> country.setNumericCode("000")),
			() -> org.junit.jupiter.api.Assertions.assertEquals("000", country.getNumericCode())
		);
	}
}
