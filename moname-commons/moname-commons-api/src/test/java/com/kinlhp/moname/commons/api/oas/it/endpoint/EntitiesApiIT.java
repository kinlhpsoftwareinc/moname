package com.kinlhp.moname.commons.api.oas.it.endpoint;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.kinlhp.moname.commons.api.oas.endpoint.EntitiesApi;

// import com.kinlhp.moname.commons.api.oas.TestConfiguration;

// @ContextConfiguration(classes = {TestConfiguration.class})
@WebMvcTest(controllers = {EntitiesApi.class})
class EntitiesApiIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	final void shouldGetEntitiesNotImplemented() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/entities"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isNotImplemented())
			.andExpect(MockMvcResultMatchers.jsonPath("$.detail", Matchers.equalTo("Method 'GET' is not implemented.")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.instance", Matchers.equalTo("/api/v1/entities")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo(501)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo("Not Implemented")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo("about:blank")));
	}

	@Test
	final void shouldGetPagedEntitiesNotImplemented() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/entities?page=0&size=25"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isNotImplemented())
			.andExpect(MockMvcResultMatchers.jsonPath("$.detail", Matchers.equalTo("Method 'GET' is not implemented.")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.instance", Matchers.equalTo("/api/v1/entities")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo(501)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo("Not Implemented")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo("about:blank")));
	}

	@Test
	final void shouldGetEntitiesMethodNotAllowed() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/entities"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
			.andExpect(MockMvcResultMatchers.jsonPath("$.detail", Matchers.equalTo("Method 'POST' is not supported.")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.instance", Matchers.equalTo("/api/v1/entities")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo(405)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo("Method Not Allowed")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo("about:blank")));
	}

	@Test
	final void shouldGetEntityNotImplemented() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/entities/1"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isNotImplemented())
			.andExpect(MockMvcResultMatchers.jsonPath("$.detail", Matchers.equalTo("Method 'GET' is not implemented.")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.instance", Matchers.equalTo("/api/v1/entities/%7Bid%7D")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo(501)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo("Not Implemented")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo("about:blank")));
	}

	@Test
	final void shouldGetEntityBadRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/entities/a"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.detail", Matchers.equalTo("Failed to convert 'id' with value: 'a'")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.instance", Matchers.equalTo("/api/v1/entities/a")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo(400)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo("Bad Request")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo("about:blank")));
	}

}
