package com.kinlhp.moname.commons.api.oas.endpoint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = {V1ApiController.class})
class V1ApiIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	final void shouldNotViolateConstraints() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/entities"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isNotImplemented());
	}

}
