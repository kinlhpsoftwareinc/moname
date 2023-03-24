package com.kinlhp.moname.addressing.domain.country;

import javax.sql.DataSource;

import org.assertj.db.type.Request;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.kinlhp.moname.addressing.domain.TestConfiguration;

import static org.assertj.db.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = {TestConfiguration.class})
@DataJpaTest
class EntityIT {

	@Autowired
	private DataSource dataSource;

	@DisplayName(value = "Count.")
	@Test
	final void shouldCount() {
		final var request = new Request(dataSource, "SELECT COUNT(numeric_code) AS count FROM country");
		assertThat(request)
			.hasNumberOfRows(1)
			.hasNumberOfColumns(1)
			.row()
			.value().isEqualTo(249);
	}

	@DisplayName(value = "Countries.")
	@Test
	final void shouldRetrieve() {
		final var table = new Table(dataSource, "country");
		assertThat(table)
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

}
