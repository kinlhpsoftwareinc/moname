package com.kinlhp.moname.commons.test.spring.jpa;

import javax.sql.DataSource;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

final class SimpleDataJpaTestSliceIT extends AbstractDataJpaTestSliceIT {

	@Autowired
	private DataSource dataSource;

	@Autowired(required = false)
	private @Nullable MockMvc mockMvc;

	@Test
	void assertDataJpaSlice() {
		Assertions.assertNotNull(dataSource);
	}

	@Test
	void assertNonDataJpaSlice() {
		Assertions.assertNull(mockMvc);
	}
}
