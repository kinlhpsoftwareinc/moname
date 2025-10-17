package com.kinlhp.moname.commons.api.spring.configuration.security;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.client.RestTemplate;

import com.kinlhp.moname.commons.api.spring.configuration.security.authentication.KeycloakJwtGrantedAuthoritiesConverter;

/**
 * <a href=https://www.youtube.com/watch?v=68azMcqPpyo>O que você deveria saber sobre Oauth 2.0 e OpenID!</a>
 * <br/>
 * <a href=https://www.youtube.com/watch?v=EQ5EwIYsgIE>Aulão: Proteja o acesso aos seus serviços web com o Spring Security 6!</a>
 * <br/>
 * <a href=https://www.youtube.com/watch?v=kEJ8a1w4a2Q>Aulão: Spring Security 6 + JWT!</a>
 * <br/>
 * <a href=https://www.youtube.com/watch?v=vV2NdanynpA>OAuth + OpenID com Spring Security e Keycloak!</a>
 * <br/>
 * <a href=https://www.youtube.com/watch?v=lWLffhSjxZc>Access Token Pattern com Spring Security Oauth e Spring Cloud Gateway!</a>
 * <br/>
 * <a href=https://www.youtube.com/watch?v=OTl2hyeEVv0>Guia COMPLETO de Segurança para SPA com React, Java e Spring</a>
 * <br/>
 * <br/>
 * <a href=https://www.baeldung.com/spring-boot-keycloak>A Quick Guide to Using Keycloak With Spring Boot</a>
 */
// TODO: Enable and disable by profile (profile security -> application-security.yaml)
@AutoConfiguration
@EnableMethodSecurity
@EnableWebSecurity
@Profile("!security")
public class WebNonSecurityAutoConfiguration {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(WebNonSecurityAutoConfiguration.class);

	private final boolean corsEnabled;
	private final boolean csrfEnabled;
	private final String sslBundleName;

	public WebNonSecurityAutoConfiguration(
			@Value(/*// TODO: Rename property*/"${cors:false}") final boolean corsEnabled,
			@Value(/*// TODO: Rename property*/"${csrf:false}") final boolean csrfEnabled,
			@Value(/*// TODO: Rename property*/"${bundle:truststore-ed25519-p12}") final String sslBundleName
	) {
		this.corsEnabled = corsEnabled;
		this.csrfEnabled = csrfEnabled;
		this.sslBundleName = sslBundleName;
	}

	@PostConstruct
	public void postConstruct() {
		LOG.debug("Autoconfiguring web non-security");
	}

	@Bean // TODO: Move to an autoconfigure of "keycloak" profile
	@Nonnull
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		LOG.debug("Configuring an extractor of GrantedAuthority to Keycloak JWT token");
		@Nonnull final var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
		return jwtAuthenticationConverter;
	}

	/**
	 * <a href="README#spring-boot">SSL</a>
	 */
	//@ConditionalOnBean({RestTemplateBuilder.class, SslBundles.class})
	@Bean // TODO: Move to an autoconfigure of "keycloak" profile
	@Nonnull
	public RestTemplate restTemplate(@Autowired @Nonnull final RestTemplateBuilder restTemplateBuilder, @Autowired @Nonnull final SslBundles sslBundles) {
		LOG.debug("Building rest template containing {} SSL bundle", sslBundleName);
		return restTemplateBuilder.setSslBundle(sslBundles.getBundle(sslBundleName)).build();
	}

	//@ConditionalOnBean(HttpSecurity.class)
	@Bean
	@Nonnull
	@SuppressWarnings("unused")
	public SecurityFilterChain securityFilterChain(@Autowired @Nonnull final HttpSecurity httpSecurity) throws Exception {
		LOG.debug("Configuring web based security");
		return httpSecurity
				.authorizeHttpRequests(this::authorizeHttpRequests)
				.cors(this::cors)
				.csrf(this::csrf)
				.httpBasic(this::httpBasic)
				.oauth2ResourceServer(this::oauth2ResourceServer)
				.sessionManagement(this::sessionManagement)
				.build();
	}

	private void authorizeHttpRequests(@Nonnull final AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
		LOG.debug("Configuring URL based authorization");
		authorizationManagerRequestMatcherRegistry
				.requestMatchers(requestMatchersPermitAll()).permitAll()
				.anyRequest().authenticated();
	}

	private void cors(@Nonnull final CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
		LOG.debug("Configuring CORS pre-flight requests");
		if (!corsEnabled) {
			LOG.debug("Disabling CORS");
			httpSecurityCorsConfigurer.disable();
		}
	}

	private void csrf(@Nonnull final CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
		LOG.debug("Configuring CSRF protection for the methods");
		httpSecurityCsrfConfigurer.ignoringRequestMatchers(requestMatchersPermitAll());
		if (!csrfEnabled) {
			LOG.debug("Disabling CSRF");
			httpSecurityCsrfConfigurer.disable();
		}
	}

	/**
	 * @see Customizer#withDefaults() withDefaults
	 */
	@SuppressWarnings("java:S1186")
	private void httpBasic(@Nonnull final HttpBasicConfigurer<HttpSecurity> httpSecurityHttpBasicConfigurer) {
		LOG.debug("Configuring HTTP basic based authentication");
	}

	private void oauth2ResourceServer(@Nonnull final OAuth2ResourceServerConfigurer<HttpSecurity> httpSecurityOAuth2ResourceServerConfigurer) {
		httpSecurityOAuth2ResourceServerConfigurer.jwt(this::jwt);
	}

	private void sessionManagement(@Nonnull final SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer) {
		httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Nonnull
	private RequestMatcher requestMatchersPermitAll() {
		return new AntPathRequestMatcher("/", HttpMethod.GET.name());
	}

	private void jwt(OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer jwtConfigurer) {
		LOG.debug("Configuring OAuth2 resource server support");
	}
}
