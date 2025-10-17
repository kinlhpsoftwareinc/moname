package com.kinlhp.moname.commons.jpa.type.descriptor.jdbc;

import java.io.Serial;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.UUID;

import jakarta.annotation.Nonnull;

import com.github.f4b6a3.ulid.Ulid;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.UUIDJavaType;
import org.hibernate.type.descriptor.jdbc.BasicBinder;
import org.hibernate.type.descriptor.jdbc.BasicExtractor;
import org.hibernate.type.descriptor.jdbc.JdbcLiteralFormatter;
import org.hibernate.type.descriptor.jdbc.JdbcType;

import com.kinlhp.moname.commons.jpa.type.descriptor.java.UlidJavaType;
import com.kinlhp.moname.commons.jpa.type.descriptor.jdbc.internal.JdbcLiteralFormatterUlidData;

import static org.hibernate.type.SqlTypes.OTHER;

/**
 * @see org.hibernate.type.descriptor.jdbc.UUIDJdbcType UUIDJdbcType
 */
public class UlidJdbcType implements JdbcType {

	@Serial
	private static final long serialVersionUID = -5121514013814648296L;

	@SuppressWarnings("java:S3077")
	private static volatile UlidJdbcType singleton = null;

	@Nonnull
	public static UlidJdbcType getSingleton() {
		if (singleton == null) {
			synchronized (UlidJdbcType.class) {
				if (singleton == null) {
					singleton = new UlidJdbcType();
				}
			}
		}
		return singleton;
	}

	private UlidJdbcType() {
	}

	@Override
	public int getJdbcTypeCode() {
		return OTHER;
	}

	@Override
	public int getDefaultSqlTypeCode() {
		// TODO: Allow ULIDs to be saved in the database as binary of size 16 without having to be wrapped in UUID.
		//  https://discourse.hibernate.org/t/some-hibernatw-6-0-changes-in-types-the-point-of-which-i-dont-understand/8924
		return SqlTypes.UUID;
	}

	@Nonnull
	@Override
	public String toString() {
		return "UlidJdbcType";
	}

	@Nonnull
	@Override
	public Class<?> getPreferredJavaTypeClass(@Nonnull final WrapperOptions options) {
		return UUID.class;
	}

	@Nonnull
	@Override
	public <T> JdbcLiteralFormatter<T> getJdbcLiteralFormatter(@Nonnull final JavaType<T> javaType) {
		return new JdbcLiteralFormatterUlidData<>(javaType);
	}

	@Nonnull
	@Override
	public <T> ValueBinder<T> getBinder(@Nonnull final JavaType<T> javaType) {
		return new BasicBinder<>(javaType, this) {

			@Serial
			private static final long serialVersionUID = 7976076737884078871L;

			@Override
			protected void doBind(@Nonnull final PreparedStatement st, @Nonnull final T value, final int index,
					@Nonnull final WrapperOptions options) throws SQLException {
				// TODO: Allow ULIDs to be saved in the database as binary of size 16 without having to be wrapped in UUID.
				//  https://discourse.hibernate.org/t/some-hibernatw-6-0-changes-in-types-the-point-of-which-i-dont-understand/8924
				@Nonnull final var unwrappedValue = getUnwrappedValue(value, getJavaType(), options);
				st.setObject(index, unwrappedValue);
			}

			@Override
			protected void doBind(@Nonnull final CallableStatement st, @Nonnull final T value,
					@Nonnull final String name, @Nonnull final WrapperOptions options) throws SQLException {
				// TODO: Allow ULIDs to be saved in the database as binary of size 16 without having to be wrapped in UUID.
				//  https://discourse.hibernate.org/t/some-hibernatw-6-0-changes-in-types-the-point-of-which-i-dont-understand/8924
				@Nonnull final var unwrappedValue = getUnwrappedValue(value, getJavaType(), options);
				st.setObject(name, unwrappedValue);
			}
		};
	}

	@Nonnull
	@Override
	public <T> ValueExtractor<T> getExtractor(@Nonnull final JavaType<T> javaType) {
		return new BasicExtractor<>(javaType, this) {

			@Serial
			private static final long serialVersionUID = -3322417461697380137L;

			@Nonnull
			@Override
			protected T doExtract(@Nonnull final ResultSet rs, final int paramIndex,
					@Nonnull final WrapperOptions options) throws SQLException {
				@Nonnull final Class<T> typeToWrap = determineTypeToWrap(rs.getMetaData(), paramIndex);
				return getJavaType().wrap(rs.getObject(paramIndex, typeToWrap), options);
			}

			@Nonnull
			@Override
			protected T doExtract(@Nonnull final CallableStatement statement, final int index,
					@Nonnull final WrapperOptions options) throws SQLException {
				@Nonnull final Class<T> typeToWrap = determineTypeToWrap(statement.getMetaData(), index);
				return getJavaType().wrap(statement.getObject(index, typeToWrap), options);
			}

			@Nonnull
			@Override
			protected T doExtract(@Nonnull final CallableStatement statement, @Nonnull final String name,
					@Nonnull final WrapperOptions options) throws SQLException {
				@Nonnull final Class<T> typeToWrap = determineTypeToWrap(statement, name);
				return getJavaType().wrap(statement.getObject(name, typeToWrap), options);
			}
		};
	}

	@Nonnull
	@SuppressWarnings("java:S3358")
	private <T, R> R getUnwrappedValue(@Nonnull final T value, @Nonnull final JavaType<T> javaType,
			@Nonnull final WrapperOptions options) {
		//noinspection unchecked
		return (value instanceof @Nonnull final Ulid ulid)
				? (R) UlidJavaType.getSingleton().unwrap(ulid, byte[].class, options)
				: (value instanceof @Nonnull final UUID uuid)
				? (R) UUIDJavaType.INSTANCE.unwrap(uuid, byte[].class, options)
				: (R) javaType.unwrap(value, value.getClass(), options);
	}

	@Nonnull
	private <T> Class<T> determineTypeToWrap(@Nonnull final ResultSetMetaData metaData, final int index)
			throws SQLException {
		@Nonnull final var columnClassName = metaData.getColumnClassName(index);
		try {
			//noinspection unchecked
			return Ulid.class.getName().equals(columnClassName)
					? (Class<T>) UUID.class
					: (Class<T>) Class.forName(columnClassName);
		} catch (@Nonnull final ClassNotFoundException exception) {
			throw new SQLException(exception);
		}
	}

	@Nonnull
	private <T> Class<T> determineTypeToWrap(@Nonnull final CallableStatement statement, @Nonnull final String name)
			throws SQLException {
		// TODO: From name, get index and return `determineTypeToWrap(statement.getMetaData(), index);`
		//noinspection unchecked
		return (Class<T>) Ulid.class;
	}
}
