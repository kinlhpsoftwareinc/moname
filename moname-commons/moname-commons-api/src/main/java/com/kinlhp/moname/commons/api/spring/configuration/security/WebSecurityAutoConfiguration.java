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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;

import com.kinlhp.moname.commons.api.spring.configuration.security.authentication.KeycloakJwtGrantedAuthoritiesConverter;

@AutoConfiguration
@EnableMethodSecurity
@EnableWebSecurity
@Profile("security")
public class WebSecurityAutoConfiguration {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(WebSecurityAutoConfiguration.class);

	private final boolean corsEnabled;
	private final boolean csrfEnabled;

	public WebSecurityAutoConfiguration(@Value(/*// TODO: Rename property*/"${cors:false}") final boolean corsEnabled,
			@Value(/*// TODO: Rename property*/"${csrf:false}") final boolean csrfEnabled) {
		this.corsEnabled = corsEnabled;
		this.csrfEnabled = csrfEnabled;
	}

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Autoconfiguring web security");
	}

	@Bean // TODO: Move to an autoconfigure of "keycloak" profile
	@Nonnull
	@Profile("keycloak")
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		LOG.debug("Configuring an extractor of GrantedAuthority to Keycloak JWT token");
		@Nonnull final var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
		return jwtAuthenticationConverter;
	}

	@Bean
	@Nonnull
	@SuppressWarnings("unused")
	SecurityFilterChain securityFilterChain(@Autowired @Nonnull final HttpSecurity httpSecurity) throws Exception {
		LOG.debug("Configuring web based security");
		return httpSecurity
				.authorizeHttpRequests(this::authorizeHttpRequests)
				.cors(this::cors)
				.csrf(this::csrf)
				.oauth2ResourceServer(this::oauth2ResourceServer)
				.sessionManagement(this::sessionManagement)
				.build();
	}

	private void authorizeHttpRequests(@Nonnull final AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
		LOG.debug("Configuring URL based authorization");
		authorizationManagerRequestMatcherRegistry
				.requestMatchers(actuatorAllowlist("requests")).permitAll()
				.requestMatchers(actuatorAuthenticated()).hasRole("ACTUATOR")
				.anyRequest().authenticated();
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

	private void oauth2ResourceServer(@Nonnull final OAuth2ResourceServerConfigurer<HttpSecurity> httpSecurityOAuth2ResourceServerConfigurer) {
		httpSecurityOAuth2ResourceServerConfigurer.jwt(this::jwt);
	}

	private void sessionManagement(@Nonnull final SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer) {
		httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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

	private void jwt(@Nonnull final OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer jwtConfigurer) {
		LOG.debug("Configuring OAuth2 resource server support");
	}
}
