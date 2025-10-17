package com.kinlhp.moname.commons.api.spring.security.autoconfigure.cors;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * @see org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties CorsEndpointProperties
 */
@ConfigurationProperties("application.spring.security.cors")
public record CorsProperties(@DefaultValue("true") @Name("enabled") boolean isEnabled,
		@DefaultValue AccessControl accessControl) {

	public record AccessControl(
			@DefaultValue List<String> allowedOrigins,
			@DefaultValue List<String> allowedOriginPatterns,
			@DefaultValue List<String> allowedMethods,
			@DefaultValue List<String> allowedHeaders,
			@DefaultValue List<String> exposedHeaders,
			@Nullable Boolean allowCredentials,
			@DefaultValue("1800") @DurationUnit(SECONDS) Duration maxAge) {
	}

	public Optional<CorsConfiguration> toCorsConfiguration() {
		if (CollectionUtils.isEmpty(accessControl().allowedOrigins())
				&& CollectionUtils.isEmpty(accessControl().allowedOriginPatterns())) {
			return Optional.empty();
		}
		final var propertyMapper = PropertyMapper.get();
		final var corsConfiguration = new CorsConfiguration();
		propertyMapper.from(accessControl()::allowedOrigins).to(corsConfiguration::setAllowedOrigins);
		propertyMapper.from(accessControl()::allowedOriginPatterns).to(corsConfiguration::setAllowedOriginPatterns);
		propertyMapper.from(accessControl()::allowedHeaders).whenNot(CollectionUtils::isEmpty)
				.to(corsConfiguration::setAllowedHeaders);
		propertyMapper.from(accessControl()::allowedMethods).whenNot(CollectionUtils::isEmpty)
				.to(corsConfiguration::setAllowedMethods);
		propertyMapper.from(accessControl()::exposedHeaders).whenNot(CollectionUtils::isEmpty)
				.to(corsConfiguration::setExposedHeaders);
		propertyMapper.from(accessControl()::maxAge).whenNonNull().as(Duration::getSeconds)
				.to(corsConfiguration::setMaxAge);
		propertyMapper.from(accessControl()::allowCredentials).whenNonNull().to(corsConfiguration::setAllowCredentials);
		return Optional.of(corsConfiguration);
	}
}
