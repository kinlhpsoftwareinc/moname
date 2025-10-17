package com.kinlhp.moname.commons.api.spring.configuration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.annotation.PostConstruct;

@AutoConfiguration
@Profile("!security")
public class WebNonSecurityAutoConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(WebNonSecurityAutoConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Autoconfiguring web non-security");
	}

	@Bean
	@SuppressWarnings("unused")
	SecurityFilterChain securityFilterChain(final @Autowired HttpSecurity httpSecurity) throws Exception {
		LOG.debug("Configuring web based non-security");
		return httpSecurity
				.authorizeHttpRequests(this::authorizeHttpRequests)
				.cors(this::cors)
				.csrf(this::csrf)
				.build();
	}

	private void authorizeHttpRequests(final AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
		LOG.debug("Configuring URL based authorization");
		authorizationManagerRequestMatcherRegistry
				.requestMatchers(actuatorAllowlist("requests")).permitAll()
				.requestMatchers(actuatorAuthenticated()).hasRole("ACTUATOR")
				.anyRequest().permitAll();
	}

	private void cors(final CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
		LOG.debug("Configuring CORS pre-flight requests");
		LOG.warn("Disabling CORS, as it has been implicitly disabled by the non-security profile");
		httpSecurityCorsConfigurer.disable();
	}

	@SuppressWarnings("java:S4502")
	private void csrf(final CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
		LOG.debug("Configuring CSRF protection for methods");
		httpSecurityCsrfConfigurer.ignoringRequestMatchers(actuatorAllowlist("CSRF"));
		LOG.warn("Disabling CSRF, as it has been implicitly disabled by the non-security profile");
		httpSecurityCsrfConfigurer.disable();
	}

	private RequestMatcher actuatorAllowlist(final String contextDescription) {
		LOG.debug("Configuring actuator's endpoints allowlist for {}", contextDescription);
		final var allowlist = EndpointRequest.to(HealthEndpoint.class, InfoEndpoint.class);
		LOG.trace("Configured actuator's endpoints allowlist for {}: {}", contextDescription, allowlist);
		return allowlist;
	}

	private RequestMatcher actuatorAuthenticated() {
		return EndpointRequest.toAnyEndpoint();
	}
}
