package com.kinlhp.moname.commons.jpa.it;

import jakarta.annotation.Nonnull;
import javax.sql.DataSource;

import org.assertj.db.type.AssertDbConnectionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.ReadOnlyEntityException;
import com.kinlhp.moname.commons.jpa.entity.ReadOnly;
import com.kinlhp.moname.commons.jpa.repository.ReadOnlyRepository;
import com.kinlhp.moname.commons.test.spring.jpa.AbstractDataJpaTestSliceIT;

/**
 * Tests for read-only entities.
 */
class AbstractReadOnlyIT extends AbstractDataJpaTestSliceIT {

	@Autowired
	@Nonnull
	private DataSource dataSource;

	@Autowired
	@Nonnull
	private ReadOnlyRepository repository;

	@DisplayName("Read-only entities cannot be persisted")
	@Test
	final void shouldNotPersist() {
		@Nonnull final var entity = ReadOnly.builder().pk(1).description('-').build();
		@Nonnull final var exception = Assertions.assertThrows(ReadOnlyEntityException.class,
				() -> repository.saveAndFlush(entity),
				"Read-only entities cannot be persisted");
		Assertions.assertEquals("ReadOnly is read-only and cannot be persisted, updated or removed", exception.getLocalizedMessage());
	}

	@DisplayName("Read-only entities cannot be removed")
	@Test
	final void shouldNotRemove() {
		@Nonnull final var entity = repository.findById(2).orElseThrow();
		@Nonnull final var exception = Assertions.assertThrows(ReadOnlyEntityException.class, () -> repository.delete(entity),
				"Read-only entities cannot be removed");
		Assertions.assertEquals("ReadOnly is read-only and cannot be persisted, updated or removed", exception.getLocalizedMessage());
	}

	@DisplayName("Read-only entities cannot be updated")
	@Test
	final void shouldNotUpdate() {
		@Nonnull final var entity = repository.findById(1).orElseThrow();
		entity.setDescription('.');
		@Nonnull final var exception = Assertions.assertThrows(ReadOnlyEntityException.class,
				() -> repository.saveAndFlush(entity),
				"Read-only entities cannot be updated");
		Assertions.assertEquals("ReadOnly is read-only and cannot be persisted, updated or removed", exception.getLocalizedMessage());
	}

	@DisplayName("Read-only entities can be retrieved") // TODO: Replace retrieved for another word
	@Test
	final void shouldRetrieve() {
		@Nonnull final var query = "SELECT * FROM read_only ORDER BY id";
		@Nonnull final var request = AssertDbConnectionFactory.of(dataSource).create().request(query).build();
		org.assertj.db.api.Assertions.assertThat(request)
				.hasNumberOfRows(2)
				.row()
				.column("id").value().isEqualTo(1)
				.column("description").value().isEqualTo("a")
				.row()
				.column("id").value().isEqualTo(2)
				.column("description").value().isEqualTo("b");
	}
}
