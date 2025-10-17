// package com.kinlhp.moname.commons.test.spring.security.configuration;
//
// import jakarta.annotation.Nonnull;
// import jakarta.annotation.PostConstruct;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.boot.autoconfigure.AutoConfiguration;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.context.annotation.Profile;
//
// @AutoConfiguration
// @ConditionalOnMissingBean(OAuth2ClientProviderProperties.class)
// @ConditionalOnProperty(name = "token-uri", prefix = "spring.security.oauth2.client.provider.keycloak")
// @EnableConfigurationProperties(OAuth2ClientProviderProperties.class)
// @Profile("security")
// public class OAuth2ClientProviderAutoConfiguration {
//
// 	@Nonnull
// 	private static final Logger LOG = LoggerFactory.getLogger(OAuth2ClientProviderAutoConfiguration.class);
//
// 	@PostConstruct
// 	public void postConstruct() {
// 		LOG.debug("Autoconfiguring OAuth2 client provider properties");
// 	}
// }
