package com.kinlhp.moname.commons.api.spring.configuration.security.authentication;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * @see org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
 * JwtGrantedAuthoritiesConverter
 */
public final class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	@Nonnull
	@Override
	@SuppressWarnings("java:S2638")
	public Collection<GrantedAuthority> convert(@Nonnull final Jwt jwtToken) {
		//noinspection unchecked
		return ((Map<String, Collection<String>>) jwtToken.getClaimAsMap("resource_access")
				.get(jwtToken.getClaimAsString("client_id")))
				.get("roles")
				.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toUnmodifiableList());
	}
}
