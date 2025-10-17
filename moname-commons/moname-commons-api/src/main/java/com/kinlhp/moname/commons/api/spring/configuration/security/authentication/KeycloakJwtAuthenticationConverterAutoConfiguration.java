package com.kinlhp.moname.commons.api.spring.configuration.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import jakarta.annotation.PostConstruct;

@AutoConfiguration
@Profile("keycloak")
public class KeycloakJwtAuthenticationConverterAutoConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(KeycloakJwtAuthenticationConverterAutoConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Autoconfiguring Keycloak JWT (JSON Web Token) authentication converter");
	}

	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		LOG.debug("Configuring an extractor of GrantedAuthority to Keycloak JWT (JSON Web Token) token");
		final var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
		return jwtAuthenticationConverter;
	}
}
