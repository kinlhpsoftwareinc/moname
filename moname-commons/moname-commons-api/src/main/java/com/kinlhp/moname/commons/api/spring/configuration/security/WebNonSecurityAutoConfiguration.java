package com.kinlhp.moname.commons.api.spring.configuration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;

@AutoConfiguration
@Profile("insecurity")
public class WebNonSecurityAutoConfiguration {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(WebNonSecurityAutoConfiguration.class);

	private final boolean corsEnabled;
	private final boolean csrfEnabled;

	public WebNonSecurityAutoConfiguration(@Value(/*// TODO: Rename property*/"${cors:false}") final boolean corsEnabled,
			@Value(/*// TODO: Rename property*/"${csrf:false}") final boolean csrfEnabled) {
		this.corsEnabled = corsEnabled;
		this.csrfEnabled = csrfEnabled;
	}

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Autoconfiguring web non-security");
	}

	@Bean
	@Nonnull
	@SuppressWarnings("unused")
	SecurityFilterChain securityFilterChain(@Autowired @Nonnull final HttpSecurity httpSecurity) throws Exception {
		LOG.debug("Configuring web based non-security");
		return httpSecurity
				.authorizeHttpRequests(this::authorizeHttpRequests)
				.cors(this::cors)
				.csrf(this::csrf)
				.build();
	}

	private void authorizeHttpRequests(@Nonnull final AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
		LOG.debug("Configuring URL based authorization");
		authorizationManagerRequestMatcherRegistry
				.requestMatchers(actuatorAllowlist("requests")).permitAll()
				.requestMatchers(actuatorAuthenticated()).hasRole("ACTUATOR")
				.anyRequest().permitAll();
	}

	private void cors(@Nonnull final CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
		LOG.debug("Configuring CORS pre-flight requests");
		if (!corsEnabled) {
			LOG.warn("Disabling CORS");
			httpSecurityCorsConfigurer.disable();
		}
	}

	@SuppressWarnings("java:S4502")
	private void csrf(@Nonnull final CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
		LOG.debug("Configuring CSRF protection for the methods");
		httpSecurityCsrfConfigurer.ignoringRequestMatchers(actuatorAllowlist("CSRF"));
		if (!csrfEnabled) {
			LOG.warn("Disabling CSRF");
			httpSecurityCsrfConfigurer.disable();
		}
	}

	@Nonnull
	private RequestMatcher actuatorAllowlist(@Nonnull final String contextDescription) {
		LOG.debug("Configuring actuator's endpoints allowlist for {}", contextDescription);
		@Nonnull final var allowlist = EndpointRequest.to(HealthEndpoint.class, InfoEndpoint.class);
		LOG.trace("Configured actuator's endpoints allowlist for {}: {}", contextDescription, allowlist);
		return allowlist;
	}

	@Nonnull
	private RequestMatcher actuatorAuthenticated() {
		return EndpointRequest.toAnyEndpoint();
	}
}
