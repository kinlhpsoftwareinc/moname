package com.kinlhp.moname.commons.api.spring.security.autoconfigure.csrf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.context.properties.bind.Name;

@ConfigurationProperties("application.spring.security.csrf")
public record CsrfProperties(@DefaultValue("true") @Name("enabled") boolean isEnabled) {
}
