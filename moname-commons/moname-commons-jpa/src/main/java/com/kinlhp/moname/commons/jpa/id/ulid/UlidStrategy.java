package com.kinlhp.moname.commons.jpa.id.ulid;

import java.io.Serial;

import jakarta.annotation.Nonnull;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

/**
 * Implements a ULID generation strategy as defined by the {@link UlidCreator#getUlid() getUlid} method.
 */
public class UlidStrategy implements UlidValueGenerator {

	@Serial
	private static final long serialVersionUID = 4550647019469036570L;

	@SuppressWarnings("java:S3077")
	private static volatile UlidStrategy singleton = null;

	@Nonnull
	public static UlidStrategy getSingleton() {
		if (singleton == null) {
			synchronized (UlidStrategy.class) {
				if (singleton == null) {
					singleton = new UlidStrategy();
				}
			}
		}
		return singleton;
	}

	private UlidStrategy() {
	}

	/**
	 * <a href=https://github.com/f4b6a3/ulid-creator#ulid>ULID</a>
	 */
	@Nonnull
	@Override
	public Ulid generateUlid(@Nonnull final SharedSessionContractImplementor session) {
		return UlidCreator.getUlid();
	}
}
