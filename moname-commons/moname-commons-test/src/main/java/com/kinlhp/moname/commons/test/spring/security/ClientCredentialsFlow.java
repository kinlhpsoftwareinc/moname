package com.kinlhp.moname.commons.test.spring.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtendWith;

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
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface ClientCredentialsFlow {

	String clientId() default MONAME + "-commons-api";

	String clientSecret() default MONAME;

	String grantType() default "client_credentials";

	record Token(String accessToken, long expiresIn, long notBeforePolicy, long refreshExpiresIn, String scope,
			String tokenType) {

		/**
		 * Safe private lock-splitting approach.
		 */
		private static final EmptyTokenPrivateLock EMPTY_TOKEN_PRIVATE_LOCK = new EmptyTokenPrivateLock();

		private static volatile @Nullable Token empty = null;

		public static Token empty() {
			// The local variable `readingReducer` is not merely cosmetic. It reduces the number of reads of the
			// volatile field from two or three to one on the fast path. It is a micro-optimization, but it comes for
			// free.
			var readingReducer = empty;
			if (readingReducer == null) {
				synchronized (EMPTY_TOKEN_PRIVATE_LOCK) {
					readingReducer = empty;
					if (readingReducer == null) {
						try {
							readingReducer = new Token();
							empty = readingReducer;
						} catch (final Exception exception) {
							empty = null;
							throw exception;
						}
					}
				}
			}
			return readingReducer;
		}

		private Token() {
			this("", 0, 0, 0, "", "");
		}

		public Token(
				final @JsonProperty("access_token") String accessToken,
				final @JsonProperty("expires_in") long expiresIn,
				final @JsonProperty("not-before-policy") long notBeforePolicy,
				final @JsonProperty("refresh_expires_in") long refreshExpiresIn,
				final @JsonProperty("scope") String scope,
				final @JsonProperty("token_type") String tokenType
		) {
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

		public String typedAccessToken() {
			return "%s %s".formatted(tokenType(), accessToken());
		}

		/**
		 * Safe private lock-splitting approach.
		 *
		 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">
		 * The "Double-Checked Locking is Broken" Declaration
		 * </a>
		 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html">
		 * JSR 133 (Java Memory Model) FAQ
		 * </a>
		 * @see <a href="https://docs.oracle.com/javase/specs/jls/se24/html/jls-17.html">
		 * Chapter 17. Threads and Locks
		 * </a>
		 * @see <a href="https://jcp.org/en/jsr/detail?id=133">
		 * JSR 133: JavaTM Memory Model and Thread Specification Revision
		 * </a>
		 * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel">The Java Memory Model</a>
		 */
		@SuppressWarnings("java:S2094")
		private static final class EmptyTokenPrivateLock {
		}
	}
}
