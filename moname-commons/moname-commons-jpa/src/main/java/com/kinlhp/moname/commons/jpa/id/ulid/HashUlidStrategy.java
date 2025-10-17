package com.kinlhp.moname.commons.jpa.id.ulid;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator;

/**
 * Implements a hash ULID generation strategy as defined by the
 * {@link UlidCreator#getHashUlid(long, String) getHashUlid} method.
 */
public class HashUlidStrategy implements UlidValueGenerator {

	@Serial
	private static final long serialVersionUID = -1946264531305652142L;

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(HashUlidStrategy.class);

	@SuppressWarnings("java:S3077")
	private static volatile HashUlidStrategy singleton = null;

	@Nonnull
	public static HashUlidStrategy getSingleton() {
		if (singleton == null) {
			synchronized (HashUlidStrategy.class) {
				if (singleton == null) {
					singleton = new HashUlidStrategy();
				}
			}
		}
		return singleton;
	}

	private HashUlidStrategy() {
	}

	/**
	 * @see UlidCreator#getHashUlid(long, String) getHashUlid
	 */
	@Nonnull
	@Override
	public Ulid generateUlid(@Nonnull final SharedSessionContractImplementor session) {
		final var currentTimeMillis = System.currentTimeMillis();
		return UlidCreator.getHashUlid(currentTimeMillis, String.valueOf(currentTimeMillis));
	}

	/**
	 * @see UlidCreator#getHashUlid(long, String) getHashUlid
	 */
	@Nonnull
	public Ulid generateUlid(@Nonnull final SharedSessionContractImplementor session,
			@Nullable final UlidGenerator config) {
		var time = System.currentTimeMillis();
		@Nonnull var string = String.valueOf(time);
		if (config != null) {
			time = config.time() > 0L ? config.time() : time;
			string = StringUtils.isNotBlank(config.string()) ? config.string() : string;
		}
		LOG.trace("A Hash ULID will be generated with time {} and string {}", time, string);
		return UlidCreator.getHashUlid(time, string);
	}
}
