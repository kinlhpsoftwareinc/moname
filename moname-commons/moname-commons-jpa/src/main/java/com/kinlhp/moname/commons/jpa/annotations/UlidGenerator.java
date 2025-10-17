package com.kinlhp.moname.commons.jpa.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.annotation.Nonnull;

import org.hibernate.Incubating;
import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.ValueGenerationType;

import com.kinlhp.moname.commons.jpa.id.ulid.UlidValueGenerator;
import com.kinlhp.moname.commons.jpa.type.descriptor.jdbc.UlidJdbcType;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @apiNote Members of type {@link com.github.f4b6a3.ulid.Ulid Ulid} and {@link java.util.UUID UUID} are treated as
 * binary of size 16 in the JDBC layer.
 * @see org.hibernate.annotations.UuidGenerator @UuidGenerator
 */
@IdGeneratorType(com.kinlhp.moname.commons.jpa.id.ulid.UlidGenerator.class)
@JdbcType(UlidJdbcType.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD})
@ValueGenerationType(generatedBy = com.kinlhp.moname.commons.jpa.id.ulid.UlidGenerator.class)
public @interface UlidGenerator {

	/**
	 * Allows to provide a specific, generally custom, value generation implementation.
	 *
	 * @apiNote If algorithm is specified, it is expected that {@link #style() style} be {@link Style#AUTO AUTO}.
	 */
	@Incubating
	@Nonnull
	Class<? extends UlidValueGenerator> algorithm() default UlidValueGenerator.class;

	/**
	 * @see com.github.f4b6a3.ulid.UlidCreator#getHashUlid(long, String) getHashUlid
	 */
	@Nonnull
	String string() default "";

	/**
	 * Specifies which {@link Style Style} of ULID generation should be used.
	 */
	@Nonnull
	Style style() default Style.AUTO;

	/**
	 * @see com.github.f4b6a3.ulid.UlidCreator#getHashUlid(long, String) getHashUlid
	 */
	@SuppressWarnings("unused")
	long time() default 0L;

	/**
	 * Represents a kind of ULID.
	 */
	enum Style {
		/**
		 * Defaults to {@link #MONOTONIC MONOTONIC}
		 */
		AUTO,
		/**
		 * <a href=https://github.com/f4b6a3/ulid-creator#hash-ulid>Hash ULID</a>
		 *
		 * @apiNote The member's declaring class must be an implementation of
		 * {@link com.kinlhp.moname.commons.jpa.id.ulid.HashableUlid HashableUlid}
		 * @see com.github.f4b6a3.ulid.UlidCreator#getHashUlid(long, String) getHashUlid
		 */
		HASH,
		/**
		 * <a href=https://github.com/f4b6a3/ulid-creator#monotonic-ulid>Monotonic ULID</a>
		 */
		MONOTONIC,
		/**
		 * <a href=https://github.com/f4b6a3/ulid-creator#ulid>ULID</a>
		 */
		ULID
	}
}
