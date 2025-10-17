package com.kinlhp.moname.commons.jpa.id.ulid;

import java.io.Serial;

import jakarta.annotation.Nonnull;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

/**
 * Implements a monotonic ULID generation strategy as defined by the
 * {@link UlidCreator#getMonotonicUlid() getMonotonicUlid} method.
 */
public class MonotonicUlidStrategy implements UlidValueGenerator {

	@Serial
	private static final long serialVersionUID = 2042698215804416944L;

	@SuppressWarnings("java:S3077")
	private static volatile MonotonicUlidStrategy singleton = null;

	@Nonnull
	public static MonotonicUlidStrategy getSingleton() {
		if (singleton == null) {
			synchronized (MonotonicUlidStrategy.class) {
				if (singleton == null) {
					singleton = new MonotonicUlidStrategy();
				}
			}
		}
		return singleton;
	}

	private MonotonicUlidStrategy() {
	}

	/**
	 * <a href=https://github.com/f4b6a3/ulid-creator#monotonic-ulid> Monotonic ULID</a>
	 */
	@Nonnull
	@Override
	public Ulid generateUlid(@Nonnull final SharedSessionContractImplementor session) {
		return UlidCreator.getMonotonicUlid();
	}
}
