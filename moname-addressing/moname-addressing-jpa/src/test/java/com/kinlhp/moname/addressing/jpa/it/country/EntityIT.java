package com.kinlhp.moname.addressing.jpa.it.country;

import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.AssertDbConnectionFactory;
import org.assertj.db.type.Table;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.List;

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
		@Nonnull final var requestBuilder = AssertDbConnectionFactory.of(proxyDataSource()).create().request(query);
		Assertions.assertThat(requestBuilder.build())
				.hasNumberOfRows(1)
				.hasNumberOfColumns(1)
				.row()
				.value().isEqualTo(249);
	}

	@DisplayName("Countries")
	@Test
	final void shouldRetrieve() {
		@Nonnull final var pkAscending = new Table.Order[]{Table.Order.asc("numeric_code")};
		@Nonnull final var table = AssertDbConnectionFactory.of(proxyDataSource())
				.create()
				.table("country")
				.columnsToOrder(pkAscending)
				.build();
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

	@Nonnull
	private DataSource proxyDataSource() {
		@Nonnull final var formatter = FormatStyle.NONE.getFormatter();
		return ProxyDataSourceBuilder.create(dataSource)
				.beforeQuery((ignoredEexecutionInfo, queryInfos) -> queryExecutionListener(queryInfos, formatter))
				.build();
	}

	private void queryExecutionListener(@Nonnull final List<QueryInfo> queryInfos, @Nonnull final Formatter formatter) {
		queryInfos.forEach(queryInfo -> logQuery(queryInfo, formatter));
	}

	private void logQuery(@Nonnull final QueryInfo queryInfo, @Nonnull final Formatter formatter) {
		System.out.println(formatter.format(queryInfo.getQuery()));
	}
}
