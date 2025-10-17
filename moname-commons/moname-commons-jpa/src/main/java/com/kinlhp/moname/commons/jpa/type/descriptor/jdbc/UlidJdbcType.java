package com.kinlhp.moname.commons.jpa.type.descriptor.jdbc;

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

import java.io.Serial;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.UUID;

import com.kinlhp.moname.commons.jpa.type.descriptor.java.UlidJavaType;
import com.kinlhp.moname.commons.jpa.type.descriptor.jdbc.internal.JdbcLiteralFormatterUlidData;

import static org.hibernate.type.SqlTypes.OTHER;

/**
 * @see org.hibernate.type.descriptor.jdbc.UUIDJdbcType UUIDJdbcType
 */
public class UlidJdbcType implements JdbcType {

	@Serial
	private static final long serialVersionUID = -5121514013814648296L;

	public static UlidJdbcType getSingleton() {
		return Holder.SINGLETON;
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

	@Override
	public String toString() {
		return "UlidJdbcType";
	}

	@Override
	public Class<?> getPreferredJavaTypeClass(final WrapperOptions options) {
		return UUID.class;
	}

	@Override
	public <T> JdbcLiteralFormatter<T> getJdbcLiteralFormatter(final JavaType<T> javaType) {
		return new JdbcLiteralFormatterUlidData<>(javaType);
	}

	@Override
	public <T> ValueBinder<T> getBinder(final JavaType<T> javaType) {
		return new BasicBinder<>(javaType, this) {

			@Serial
			private static final long serialVersionUID = 7976076737884078871L;

			@Override
			protected void doBind(final PreparedStatement st, final T value, final int index,
					final WrapperOptions options) throws SQLException {
				// TODO: Allow ULIDs to be saved in the database as binary of size 16 without having to be wrapped in UUID.
				//  https://discourse.hibernate.org/t/some-hibernatw-6-0-changes-in-types-the-point-of-which-i-dont-understand/8924
				final var unwrappedValue = getUnwrappedValue(value, getJavaType(), options);
				st.setObject(index, unwrappedValue);
			}

			@Override
			protected void doBind(final CallableStatement st, final T value, final String name,
					final WrapperOptions options) throws SQLException {
				// TODO: Allow ULIDs to be saved in the database as binary of size 16 without having to be wrapped in UUID.
				//  https://discourse.hibernate.org/t/some-hibernatw-6-0-changes-in-types-the-point-of-which-i-dont-understand/8924
				final var unwrappedValue = getUnwrappedValue(value, getJavaType(), options);
				st.setObject(name, unwrappedValue);
			}
		};
	}

	@Override
	public <T> ValueExtractor<T> getExtractor(final JavaType<T> javaType) {
		return new BasicExtractor<>(javaType, this) {

			@Serial
			private static final long serialVersionUID = -3322417461697380137L;

			@Override
			protected T doExtract(final ResultSet rs, final int paramIndex, final WrapperOptions options)
					throws SQLException {
				final Class<T> typeToWrap = determineTypeToWrap(rs.getMetaData(), paramIndex);
				return getJavaType().wrap(rs.getObject(paramIndex, typeToWrap), options);
			}

			@Override
			protected T doExtract(final CallableStatement statement, final int index, final WrapperOptions options)
					throws SQLException {
				final Class<T> typeToWrap = determineTypeToWrap(statement.getMetaData(), index);
				return getJavaType().wrap(statement.getObject(index, typeToWrap), options);
			}

			@Override
			protected T doExtract(final CallableStatement statement, final String name, final WrapperOptions options)
					throws SQLException {
				final Class<T> typeToWrap = determineTypeToWrap(statement, name);
				return getJavaType().wrap(statement.getObject(name, typeToWrap), options);
			}
		};
	}

	@SuppressWarnings("java:S3358")
	private <T, R> R getUnwrappedValue(final T value, final JavaType<T> javaType, final WrapperOptions options) {
		//noinspection unchecked
		return (value instanceof final Ulid ulid)
				? (R) UlidJavaType.getSingleton().unwrap(ulid, byte[].class, options)
				: (value instanceof final UUID uuid)
				? (R) UUIDJavaType.INSTANCE.unwrap(uuid, byte[].class, options)
				: (R) javaType.unwrap(value, value.getClass(), options);
	}

	private <T> Class<T> determineTypeToWrap(final ResultSetMetaData metaData, final int index) throws SQLException {
		final var columnClassName = metaData.getColumnClassName(index);
		try {
			//noinspection unchecked
			return Ulid.class.getName().equals(columnClassName)
					? (Class<T>) UUID.class
					: (Class<T>) Class.forName(columnClassName);
		} catch (final ClassNotFoundException exception) {
			throw new SQLException(exception);
		}
	}

	private <T> Class<T> determineTypeToWrap(final CallableStatement statement, final String name) throws SQLException {
		// TODO: From name, get index and return `determineTypeToWrap(statement.getMetaData(), index);`
		//noinspection unchecked
		return (Class<T>) Ulid.class;
	}

	/**
	 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
	 */
	private static final class Holder {

		private static final UlidJdbcType SINGLETON = new UlidJdbcType();
	}
}
