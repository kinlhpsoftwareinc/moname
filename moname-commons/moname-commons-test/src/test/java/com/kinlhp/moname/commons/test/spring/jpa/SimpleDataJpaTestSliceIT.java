package com.kinlhp.moname.commons.test.spring.jpa;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

final class SimpleDataJpaTestSliceIT extends AbstractDataJpaTestSliceIT {

	@Autowired
	@Nonnull
	private DataSource dataSource;

	@Autowired
	@Nullable
	private MockMvc mockMvc;

	@Test
	void assertDataJpaSlice() {
		Assertions.assertNotNull(dataSource);
	}

	@Test
	void assertNonDataJpaSlice() {
		Assertions.assertNull(mockMvc);
	}
}
