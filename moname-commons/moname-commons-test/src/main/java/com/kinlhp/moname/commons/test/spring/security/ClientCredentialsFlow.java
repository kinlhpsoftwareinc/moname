package com.kinlhp.moname.commons.test.spring.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.extension.ExtendWith;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Instant;

import static com.kinlhp.moname.commons.test.testcontainers.SharedNetwork.MONAME;

@Documented
@ExtendWith(ClientCredentialsFlowExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface ClientCredentialsFlow {

	@Nonnull
	String clientId() default MONAME + "-commons-api";

	@Nonnull
	String clientSecret() default MONAME;

	@Nonnull
	String grantType() default "client_credentials";

	record Token(@Nonnull String accessToken, long expiresIn, long notBeforePolicy, long refreshExpiresIn,
			@Nonnull String scope, @Nonnull String tokenType) {

		@Nullable
		private static volatile Token empty = null;

		@Nonnull
		public static Token empty() {
			if (empty == null) {
				synchronized (Token.class) {
					if (empty == null) {
						empty = new Token();
					}
				}
			}
			//noinspection DataFlowIssue
			return empty;
		}

		private Token() {
			this("", 0, 0, 0, "", "");
		}

		public Token(@JsonProperty("access_token") @Nonnull final String accessToken,
				@JsonProperty("expires_in") final long expiresIn,
				@JsonProperty("not-before-policy") final long notBeforePolicy,
				@JsonProperty("refresh_expires_in") final long refreshExpiresIn,
				@JsonProperty("scope") @Nonnull final String scope,
				@JsonProperty("token_type") @Nonnull final String tokenType) {
			this.accessToken = accessToken;
			this.expiresIn = epochSecondOf(expiresIn);
			this.notBeforePolicy = notBeforePolicy;
			this.refreshExpiresIn = epochSecondOf(refreshExpiresIn);
			this.scope = scope;
			this.tokenType = tokenType;
		}

		private long epochSecondOf(final long secondsToAdd) {
			return Instant.now().plusSeconds(secondsToAdd).getEpochSecond();
		}

		public boolean expired() {
			return Instant.now().isAfter(Instant.ofEpochSecond(expiresIn));
		}

		public boolean isEmpty() {
			return this.equals(empty());
		}

		@Nonnull
		public String typedAccessToken() {
			return "%s %s".formatted(tokenType(), accessToken());
		}
	}
}
