package com.kinlhp.moname.commons.jpa.it;

import javax.sql.DataSource;

import org.assertj.db.type.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.kinlhp.moname.commons.jpa.ReadOnlyEntityException;
import com.kinlhp.moname.commons.jpa.entity.ReadOnlyEntity;
import com.kinlhp.moname.commons.jpa.repository.ReadOnlyRepository;

/**
 * Tests for read-only entities.
 */
class AbstractReadOnlyIT extends AbstractIT {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ReadOnlyRepository repository;

	@DisplayName(value = "Read-only entities cannot be persisted.")
	@Test
	final void shouldNotPersist() {
		final var entity = ReadOnlyEntity.builder().pk(0).description('-').build();
		final var exception = Assertions.assertThrows(ReadOnlyEntityException.class, () -> repository.saveAndFlush(entity),
			"Read-only entities cannot be persisted.");
		Assertions.assertEquals("ReadOnlyEntity: is read-only", exception.getLocalizedMessage());
	}

	@DisplayName(value = "Read-only entities cannot be removed.")
	@Test
	final void shouldNotRemove() {
		final var entity = repository.findById(2).orElseThrow();
		final var exception = Assertions.assertThrows(ReadOnlyEntityException.class, () -> repository.delete(entity),
			"Read-only entities cannot be removed.");
		Assertions.assertEquals("ReadOnlyEntity: is read-only", exception.getLocalizedMessage());
	}

	@DisplayName(value = "Read-only entities cannot be updated.")
	@Test
	final void shouldNotUpdate() {
		final var entity = repository.findById(1).orElseThrow();
		entity.setDescription('.');
		final var exception = Assertions.assertThrows(ReadOnlyEntityException.class, () -> repository.saveAndFlush(entity),
			"Read-only entities cannot be updated.");
		Assertions.assertEquals("ReadOnlyEntity: is read-only", exception.getLocalizedMessage());
	}

	@DisplayName(value = "Read-only entities can be retrieved.")
	@Test
	final void shouldRetrieve() {
		final var request = "SELECT * FROM read_only ORDER BY id";
		org.assertj.db.api.Assertions.assertThat(new Request(dataSource, request))
			.hasNumberOfRows(2)
			.row()
			.column("id").value().isEqualTo(1)
			.column("description").value().isEqualTo("a")
			.row()
			.column("id").value().isEqualTo(2)
			.column("description").value().isEqualTo("b");
	}

}
