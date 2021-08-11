package com.kinlhp.moname.commons.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.TransactionSystemException;

/**
 * Tests for read-only entities.
 */
@ActiveProfiles(profiles = {"test"})
@EnableAutoConfiguration
@SpringBootTest(classes = {ReadOnlyRepository.class})
class AbstractReadOnlyTests {

	@Autowired
	private ReadOnlyRepository repository;

	@DisplayName(value = "Read-only entities cannot be persisted.")
	@Test
	final void shouldNotPersist() {
		final var entity = new ReadOnlyEntity();
		entity.setPk(0);
		entity.setDescription('-');
		final var exception = Assertions.assertThrows(ReadOnlyEntityException.class, () -> repository.save(entity),
			"Read-only entities cannot be persisted.");
		Assertions.assertEquals("ReadOnlyEntity: is read-only", exception.getLocalizedMessage());
	}

	@DisplayName(value = "Read-only entities cannot be removed.")
	@Sql(statements = {"INSERT INTO read_only (id, description) VALUES (2, 'b');"})
	@Test
	final void shouldNotRemove() {
		final var entity = repository.findById(2).orElseThrow();
		final var exception = Assertions.assertThrows(ReadOnlyEntityException.class, () -> repository.delete(entity),
			"Read-only entities cannot be removed.");
		Assertions.assertEquals("ReadOnlyEntity: is read-only", exception.getLocalizedMessage());
	}

	@DisplayName(value = "Read-only entities cannot be updated.")
	@Sql(statements = {"INSERT INTO read_only (id, description) VALUES (1, 'a');"})
	@Test
	final void shouldNotUpdate() {
		final var entity = repository.findById(1).orElseThrow();
		entity.setDescription('.');
		final var exception = Assertions.assertThrows(TransactionSystemException.class, () -> repository.save(entity),
			"Read-only entities cannot be updated.").getMostSpecificCause();
		Assertions.assertEquals(ReadOnlyEntityException.class, exception.getClass(),
			"Should throw an ReadOnlyEntityException.");
		Assertions.assertEquals("ReadOnlyEntity: is read-only", exception.getLocalizedMessage());
	}

}
