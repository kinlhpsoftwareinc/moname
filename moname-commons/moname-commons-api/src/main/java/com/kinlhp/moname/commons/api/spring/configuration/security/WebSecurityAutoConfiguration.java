package com.kinlhp.moname.commons.api.spring.configuration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;

import com.kinlhp.moname.commons.api.spring.security.autoconfigure.cors.CorsProperties;
import com.kinlhp.moname.commons.api.spring.security.autoconfigure.csrf.CsrfProperties;

@AutoConfiguration
@EnableConfigurationProperties({CorsProperties.class, CsrfProperties.class})
@EnableMethodSecurity
@EnableWebSecurity
@Profile("security")
public class WebSecurityAutoConfiguration {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(WebSecurityAutoConfiguration.class);

	@Nonnull
	private final CorsProperties corsProperties;

	@Nonnull
	private final CsrfProperties csrfProperties;

	public WebSecurityAutoConfiguration(@Autowired @Nonnull final CorsProperties corsProperties,
			@Autowired @Nonnull final CsrfProperties csrfProperties) {
		this.corsProperties = corsProperties;
		this.csrfProperties = csrfProperties;
	}

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Autoconfiguring web security");
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
		if (!corsProperties.isEnabled()) {
			LOG.warn("Disabling CORS, as it has been explicitly disabled via configuration property");
			httpSecurityCorsConfigurer.disable();
			return;
		}
		corsProperties.toCorsConfiguration().ifPresentOrElse(
				corsConfiguration -> configureCors(httpSecurityCorsConfigurer, corsConfiguration),
				() -> {
					LOG.warn("""
							Although CORS is explicitly enabled via configuration property, neither allowed origins nor \
							allowed origin patterns is configured; therefore, CORS is being disabled""");
					httpSecurityCorsConfigurer.disable();
				}
		);
	}

	private void configureCors(@Nonnull final CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer,
			@Nonnull final CorsConfiguration corsConfiguration) {
		@Nonnull final var corsConfigurationSource = new UrlBasedCorsConfigurationSource();
		corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource);
	}

	@SuppressWarnings("java:S4502")
	private void csrf(@Nonnull final CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
		LOG.debug("Configuring CSRF protection for methods");
		httpSecurityCsrfConfigurer.ignoringRequestMatchers(actuatorAllowlist("CSRF"));
		if (!csrfProperties.isEnabled()) {
			LOG.warn("Disabling CSRF, as it has been explicitly disabled via configuration property");
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
