package com.kinlhp.moname.commons.api.oas.it.endpoint;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.ssl.SslAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;

import com.kinlhp.moname.commons.api.oas.endpoint.EntitiesApi;
import com.kinlhp.moname.commons.api.spring.configuration.security.WebSecurityAutoConfiguration;
import com.kinlhp.moname.commons.test.spring.security.ClientCredentialsFlow;
import com.kinlhp.moname.commons.test.testcontainers.autoconfigure.keycloak.KeycloakAutoConfiguration;

/**
 * <a href="README#spring-security-test">Spring Security Test</a>
 */
//@ActiveProfiles({"test", "mysql", "security", "keycloak", "keycloak-optimized"})
@AutoConfigureWebClient
@ImportAutoConfiguration({KeycloakAutoConfiguration.class, SslAutoConfiguration.class,
		WebSecurityAutoConfiguration.class})
@WebMvcTest(EntitiesApi.class)
final class EntitiesApiIT {

	@Nonnull
	private final MockMvc mockMvc;

	EntitiesApiIT(@Autowired @Nonnull final MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@ClientCredentialsFlow
	@Test
	void shouldGetEntitiesNotImplemented() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/entities"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isNotImplemented())
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail", Matchers.equalTo("Method 'GET' is not implemented")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.instance", Matchers.equalTo("/v1/entities")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo(501)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo("Not Implemented")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo("about:blank")));
	}

	@ClientCredentialsFlow
	@Test
	void shouldGetPagedEntitiesNotImplemented() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/entities?page=0&size=25"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isNotImplemented())
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail", Matchers.equalTo("Method 'GET' is not implemented")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.instance", Matchers.equalTo("/v1/entities")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo(501)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo("Not Implemented")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo("about:blank")));
	}

	@ClientCredentialsFlow
	@Test
	void shouldGetEntitiesMethodNotAllowed() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/v1/entities"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail", Matchers.equalTo("Method 'POST' is not supported.")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.instance", Matchers.equalTo("/v1/entities")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo(405)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo("Method Not Allowed")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo("about:blank")));
	}

	@ClientCredentialsFlow
	@Test
	void shouldGetEntityNotImplemented() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/entities/1"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isNotImplemented())
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail", Matchers.equalTo("Method 'GET' is not implemented")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.instance", Matchers.equalTo("/v1/entities/%7Bid%7D")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo(501)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo("Not Implemented")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo("about:blank")));
	}

	@ClientCredentialsFlow
	@Test
	void shouldGetEntityBadRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/entities/a"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail", Matchers.equalTo("Failed to convert 'id' with value: 'a'")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.instance", Matchers.equalTo("/v1/entities/a")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo(400)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo("Bad Request")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo("about:blank")));
	}

	@EnableAutoConfiguration
	@ImportAutoConfiguration({DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
			HibernateJpaAutoConfiguration.class})
	@Profile("mysql & keycloak-optimized")
	@TestConfiguration
	static class OptimizedKeycloakTestConfiguration {

		@Nonnull
		private static final Logger LOG = LoggerFactory.getLogger(OptimizedKeycloakTestConfiguration.class);

		@PostConstruct
		void postConstruct() {
			LOG.debug("Data source support test configuration initialized");
		}
	}
}
