package com.kinlhp.moname.commons.jpa.id.ulid;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import jakarta.annotation.Nonnull;
import java.io.Serial;

/**
 * Implements a monotonic ULID generation strategy as defined by the
 * {@link UlidCreator#getMonotonicUlid() getMonotonicUlid} method.
 */
public class MonotonicUlidStrategy implements UlidValueGenerator {

	@Serial
	private static final long serialVersionUID = 2042698215804416944L;

	@Nonnull
	public static MonotonicUlidStrategy getSingleton() {
		return Holder.SINGLETON;
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

	/**
	 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
	 */
	private static final class Holder {

		@Nonnull
		private static final MonotonicUlidStrategy SINGLETON = new MonotonicUlidStrategy();
	}
}
