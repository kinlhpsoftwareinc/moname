package com.kinlhp.moname.commons.api.spring.configuration.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @see org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
 * JwtGrantedAuthoritiesConverter
 */
public final class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	private static final Logger LOG = LoggerFactory.getLogger(KeycloakJwtGrantedAuthoritiesConverter.class);

	@Override
	@SuppressWarnings("java:S2638")
	public Collection<GrantedAuthority> convert(final Jwt jwtToken) {
		LOG.trace("Converting Keycloak JWT (JSON Web Token) to granted authorities");
		//noinspection unchecked
		return ((Map<String, Collection<String>>) jwtToken.getClaimAsMap("resource_access")
				.get(jwtToken.getClaimAsString("client_id")))
				.get("roles")
				.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toUnmodifiableList());
	}
}
