package com.kinlhp.moname.commons.jpa.id.ulid;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serial;

/**
 * Implements a ULID generation strategy as defined by the {@link UlidCreator#getUlid() getUlid} method.
 */
public class UlidStrategy implements UlidValueGenerator {

	@Serial
	private static final long serialVersionUID = 4550647019469036570L;

	public static UlidStrategy getSingleton() {
		return Holder.SINGLETON;
	}

	private UlidStrategy() {
	}

	/**
	 * <a href=https://github.com/f4b6a3/ulid-creator#ulid>ULID</a>
	 */
	@Override
	public Ulid generateUlid(final SharedSessionContractImplementor session) {
		return UlidCreator.getUlid();
	}

	/**
	 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
	 */
	private static final class Holder {

		private static final UlidStrategy SINGLETON = new UlidStrategy();
	}
}
