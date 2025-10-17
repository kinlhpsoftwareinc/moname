package com.kinlhp.moname.commons.test.spring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import com.kinlhp.moname.commons.test.spring.security.ClientCredentialsFlow.Token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("ClientCredentialsFlow.Token")
class ClientCredentialsFlowTokenTest {

	private static Token tokenExpiringIn(final long seconds) {
		return new Token("access-token", seconds, 0L, 0L, "profile email", "Bearer");
	}

	@DisplayName("when deserialized from a Keycloak token response")
	@Nested
	class Deserialization {

		@DisplayName("maps every field and turns relative lifespans into epoch seconds")
		@Test
		final void mapsKeycloakTokenResponse() throws Exception {
			final long before = Instant.now().getEpochSecond();
			final var token = new ObjectMapper().readValue("""
					{
					  "access_token": "eyJ.payload.signature",
					  "expires_in": 300,
					  "refresh_expires_in": 0,
					  "token_type": "Bearer",
					  "not-before-policy": 7,
					  "scope": "profile email"
					}""", Token.class);
			final long after = Instant.now().getEpochSecond();
			assertAll(
					() -> assertThat(token.accessToken()).isEqualTo("eyJ.payload.signature"),
					() -> assertThat(token.tokenType()).isEqualTo("Bearer"),
					() -> assertThat(token.scope()).isEqualTo("profile email"),
					() -> assertThat(token.notBeforePolicy()).isEqualTo(7L),
					() -> assertThat(token.expiresIn()).isBetween(before + 300L, after + 300L),
					() -> assertThat(token.refreshExpiresIn()).isBetween(before, after)
			);
		}
	}

	@DisplayName("expiration")
	@Nested
	class Expiration {

		@DisplayName("is not expired before its lifespan elapses")
		@Test
		final void isNotExpiredWithinLifespan() {
			assertThat(tokenExpiringIn(300L).expired()).isFalse();
		}

		@DisplayName("is expired once its lifespan has elapsed")
		@Test
		final void isExpiredAfterLifespan() {
			assertThat(tokenExpiringIn(-60L).expired()).isTrue();
		}
	}

	@DisplayName("typed access token")
	@Nested
	class TypedAccessToken {

		@DisplayName("joins token type and access token")
		@Test
		final void joinsTokenTypeAndAccessToken() {
			assertThat(tokenExpiringIn(300L).typedAccessToken()).isEqualTo("Bearer access-token");
		}

		@DisplayName("is a single space for the empty token")
		@Test
		final void isSingleSpaceForEmptyToken() {
			assertThat(Token.empty().typedAccessToken()).isEqualTo(" ");
		}
	}

	@DisplayName("empty token")
	@Nested
	class EmptyToken {

		@DisplayName("is a singleton")
		@Test
		final void isSingleton() {
			assertThat(Token.empty()).isSameAs(Token.empty());
		}

		@DisplayName("has blank fields")
		@Test
		final void hasBlankFields() {
			final var empty = Token.empty();
			assertAll(
					() -> assertThat(empty.accessToken()).isEmpty(),
					() -> assertThat(empty.tokenType()).isEmpty(),
					() -> assertThat(empty.scope()).isEmpty(),
					() -> assertThat(empty.notBeforePolicy()).isZero()
			);
		}

		@DisplayName("is recognized as empty")
		@Test
		final void isEmpty() {
			assertThat(Token.empty().isEmpty()).isTrue();
		}

		@DisplayName("is not confused with an obtained token")
		@Test
		final void obtainedTokenIsNotEmpty() {
			assertThat(tokenExpiringIn(300L).isEmpty()).isFalse();
		}

		@DisplayName("is not confused with an expired token")
		@Test
		final void expiredTokenIsNotEmpty() {
			assertThat(tokenExpiringIn(-60L).isEmpty()).isFalse();
		}
	}
}
