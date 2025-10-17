package com.kinlhp.moname.commons.test.spring.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import jakarta.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@DisplayName("@ClientCredentialsFlow in a web application context without autoconfigured MockMvc")
@SpringJUnitWebConfig(ClientCredentialsFlowExtensionPlainWebContextIT.DenyAllSecurityConfiguration.class)
class ClientCredentialsFlowExtensionPlainWebContextIT {

	@DisplayName("builds a MockMvc with Spring Security applied and the token as default request header")
	@Test
	final void buildsMockMvcWithSpringSecurityApplied(
			@ClientCredentialsFlow(clientId = "plain-web-context@moname") @Nonnull final MockMvc mockMvc)
			throws Exception {
		@Nonnull final var result = mockMvc.perform(get("/")).andDo(print()).andReturn();
		assertAll(
				() -> assertThat(result.getResponse().getStatus()).isEqualTo(FORBIDDEN.value()),
				() -> assertThat(result.getRequest().getHeader(AUTHORIZATION)).isNull()
		);
	}

	@Configuration(proxyBeanMethods = false)
	@EnableWebMvc
	@EnableWebSecurity
	static class DenyAllSecurityConfiguration {

		@Bean
		@Nonnull
		SecurityFilterChain denyAll(@Nonnull final HttpSecurity http) throws Exception {
			return http.authorizeHttpRequests(requests -> requests.anyRequest().denyAll()).build();
		}
	}
}
