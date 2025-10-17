package com.kinlhp.moname.commons.jpa.it.ulid;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Table;
import javax.sql.DataSource;

import org.assertj.db.type.AssertDbConnectionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style;
import com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.entity.ulid.Discriminator;
import com.kinlhp.moname.commons.jpa.repository.ulid.UlidIdentifiableRepository;

import static com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable.DISCRIMINATOR_COLUMN;

@SuppressWarnings("java:S119")
public interface UlidGeneratorIT<T extends AbstractUlidIdentifiable<PK>, PK extends Serializable> {

	Logger LOG = LoggerFactory.getLogger(UlidGeneratorIT.class);

	String SQL_QUERY_TEMPLATE = "SELECT %s FROM %s WHERE id = ?";

	T createEntity();

	DataSource getDataSource();

	Style getExpectedStyle();

	UlidIdentifiableRepository<T, PK> getRepository();

	default <R> R getUnwrappedSqlParameter(final PK pk) {
		//noinspection unchecked
		return (R) pk;
	}

	/**
	 * @deprecated Isn't able to natively handle type when executes the SQL query in a PreparedStatement directly.
	 */
	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	default Set<String> supplyDirectSqlQueryBypassProfiles() {
		return Collections.emptySet();
	}

	/**
	 * @deprecated Isn't able to natively handle type when executes the SQL query in a PreparedStatement directly.
	 */
	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	default void warnBypassReason(final String profile) {
		// do nothing
	}

	@Test
	default void shouldGenerate() {
		final var entity = createEntity();
		getRepository().saveAndFlush(entity);
		doAssertions(entity);
	}

	private void doAssertions(final T entity) {
		assertPk(entity);
		assertDiscriminator(entity);
	}

	private void assertPk(final T entity) {
		Assertions.assertNotNull(entity.getPk());
		final var config = entity.getConfig();
		Assertions.assertNotNull(config.orElseThrow());
		Assertions.assertEquals(getExpectedStyle(), config.get().style());
	}

	private void assertDiscriminator(final T entity) {
		assertDiscriminatorByDirectSqlQuery(entity);
		assertDiscriminatorByRepositoryQuery(entity);
	}

	private void assertDiscriminatorByDirectSqlQuery(final T entity) {
		//if (bypass().apply(this::supplyDirectSqlQueryBypassProfiles, warnBypassReason())) return;

		final var query = SQL_QUERY_TEMPLATE.formatted(DISCRIMINATOR_COLUMN, tableOf(entity));
		final var unwrappedSqlParameter = getUnwrappedSqlParameter(entity.getPk());
		final var request = AssertDbConnectionFactory.of(getDataSource()).create().request(query)
				.parameters(unwrappedSqlParameter).build();
		org.assertj.db.api.Assertions.assertThat(request)
				.hasNumberOfRows(1)
				.row()
				.column(DISCRIMINATOR_COLUMN)
				.value()
				.isEqualTo(discriminatorValueOf(entity).name());
	}

	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	private BiFunction<Supplier<Set<String>>, Consumer<Set<String>>, Boolean> bypass() {
		return (profiles, warning) -> {
			final var bypassedProfiles = profiles.get();
			final var bypassed = !CollectionUtils.isEmpty(bypassedProfiles);
			if (bypassed) {
				try {
					warning.accept(bypassedProfiles);
				} catch (final Exception exception) {
					LOG.error("// TODO: Log", exception); // TODO: Log
					throw new RuntimeException("// TODO: Message", exception); // TODO: Message
				}
			}
			return bypassed;
		};
	}

	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	private Consumer<Set<String>> warnBypassReason() {
		return profiles -> profiles.forEach(this::warnBypassReason);
	}

	private void assertDiscriminatorByRepositoryQuery(final T entity) {
		org.assertj.core.api.Assertions.assertThat(getRepository().findById(entity.getPk()).orElseThrow())
				.extracting(T::getDiscriminator)
				.isNotNull()
				.isEqualTo(discriminatorValueOf(entity));
	}

	private String tableOf(final T entity) {
		return entity.getClass().getSuperclass().getAnnotation(Table.class).name();
	}

	private Discriminator discriminatorValueOf(final T entity) {
		final var value = entity.getClass().getAnnotation(DiscriminatorValue.class).value();
		return Discriminator.valueOf(value);
	}
}
