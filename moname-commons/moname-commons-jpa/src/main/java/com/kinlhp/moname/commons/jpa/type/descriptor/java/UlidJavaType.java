package com.kinlhp.moname.commons.jpa.type.descriptor.java;

import com.github.f4b6a3.ulid.Ulid;
import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import static org.hibernate.type.SqlTypes.ARRAY;
import static org.hibernate.type.SqlTypes.BINARY;
import static org.hibernate.type.SqlTypes.VARCHAR;

/**
 * @see org.hibernate.type.descriptor.java.UUIDJavaType UUIDJavaType
 */
public class UlidJavaType extends AbstractClassJavaType<Ulid> {

	@Serial
	private static final long serialVersionUID = -3564581031741712015L;

	private static final Logger LOG = LoggerFactory.getLogger(UlidJavaType.class);

	public static UlidJavaType getSingleton() {
		return Holder.SINGLETON;
	}

	private UlidJavaType() {
		super(Ulid.class);
	}

	@Override
	public boolean useObjectEqualsHashCode() {
		return true;
	}

	@Override
	public String toString(final Ulid value) {
		return ToStringTransformer.getSingleton().transform(value);
	}

	@Override
	public Ulid fromString(final CharSequence string) {
		return ToStringTransformer.getSingleton().parse(string);
	}

	@Override
	public long getDefaultSqlLength(final Dialect dialect, final JdbcType jdbcType) {
		return switch (jdbcType.getDdlTypeCode()) {
			case ARRAY, BINARY -> 16;
			case VARCHAR -> 26;
			default -> super.getDefaultSqlLength(dialect, jdbcType);
		};
	}

	@Override
	public <T> @Nullable T unwrap(final @Nullable Ulid value, final Class<T> type, final WrapperOptions options) {
		final var message = "Value of type %s will be unwrapped to {} value of type {}"
				.formatted(Ulid.class.getName());
		return value == null
				? null
				: switch (type) {
			case final Class<T> t when Ulid.class.isAssignableFrom(t) -> {
				LOG.trace(message, "ULID", t.getName());
				//noinspection unchecked
				yield (T) PassThroughTransformer.getSingleton().transform(value);
			}
			case final Class<T> t when UUID.class.isAssignableFrom(t) -> {
				LOG.trace(message, "UUID", t.getName());
				//noinspection unchecked
				yield (T) ToUuidTransformer.getSingleton().transform(value);
			}
			case final Class<T> t when String.class.isAssignableFrom(t) -> {
				LOG.trace(message, "string", t.getName());
				//noinspection unchecked
				yield (T) ToStringTransformer.getSingleton().transform(value);
			}
			case final Class<T> t when byte[].class.isAssignableFrom(t) -> {
				LOG.trace(message, "byte array", t.getName());
				yield (T) ToBytesTransformer.getSingleton().transform(value);
			}
			default -> throw unknownUnwrap(type);
		};
	}

	@Override
	public HibernateException unknownUnwrap(final Class<?> conversionType) {
		return super.unknownUnwrap(conversionType);
	}

	@Override
	@SuppressWarnings("java:S2259")
	public <X> @Nullable Ulid wrap(final @Nullable X value, final @Nullable WrapperOptions options) {
		final var message = "{} value of type {} will be wrapped to value of type %s".formatted(Ulid.class.getName());
		return switch (value) {
			case null -> null;
			case final Ulid ulid -> {
				LOG.trace(message, "ULID", ulid.getClass().getName());
				yield PassThroughTransformer.getSingleton().parse(ulid);
			}
			case final UUID uuid -> {
				LOG.trace(message, "UUID", uuid.getClass().getName());
				yield ToUuidTransformer.getSingleton().parse(uuid);
			}
			case final String string -> {
				LOG.trace(message, "string", string.getClass().getName());
				yield ToStringTransformer.getSingleton().parse(string);
			}
			case final byte[] bytes -> {
				LOG.trace(message, "byte array", bytes.getClass().getName());
				yield ToBytesTransformer.getSingleton().parse(bytes);
			}
			default -> throw unknownWrap(value.getClass());
		};
	}

	@Override
	protected HibernateException unknownWrap(final Class<?> conversionType) {
		return super.unknownWrap(conversionType);
	}

	public interface ValueTransformer {

		Serializable transform(final Ulid ulid);

		@SuppressWarnings("unused")
		Ulid parse(final Object value);
	}

	public static class PassThroughTransformer implements ValueTransformer {

		public static PassThroughTransformer getSingleton() {
			return Holder.SINGLETON;
		}

		private PassThroughTransformer() {
		}

		@Override
		public Ulid transform(final Ulid ulid) {
			return ulid;
		}

		@Override
		public Ulid parse(final Object value) {
			return (Ulid) value;
		}

		/**
		 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
		 */
		private static final class Holder {

			private static final PassThroughTransformer SINGLETON = new PassThroughTransformer();
		}
	}

	public static class ToUuidTransformer implements ValueTransformer {

		public static ToUuidTransformer getSingleton() {
			return Holder.SINGLETON;
		}

		private ToUuidTransformer() {
		}

		@Override
		public UUID transform(final Ulid ulid) {
			return ulid.toUuid();
		}

		@Override
		public Ulid parse(final Object value) {
			return Ulid.from((UUID) value);
		}

		/**
		 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
		 */
		private static final class Holder {

			private static final ToUuidTransformer SINGLETON = new ToUuidTransformer();
		}
	}

	public static class ToStringTransformer implements ValueTransformer {

		public static ToStringTransformer getSingleton() {
			return Holder.SINGLETON;
		}

		private ToStringTransformer() {
		}

		@Override
		public String transform(final Ulid ulid) {
			return ulid.toString();
		}

		@Override
		public Ulid parse(final Object value) {
			return Ulid.from(value.toString());
		}

		/**
		 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
		 */
		private static final class Holder {

			private static final ToStringTransformer SINGLETON = new ToStringTransformer();
		}
	}

	public static class ToBytesTransformer implements ValueTransformer {

		public static ToBytesTransformer getSingleton() {
			return Holder.SINGLETON;
		}

		private ToBytesTransformer() {
		}

		@Override
		public byte[] transform(final Ulid ulid) {
			return ulid.toBytes();
		}

		@Override
		public Ulid parse(final Object value) {
			return Ulid.from((byte[]) value);
		}

		/**
		 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
		 */
		private static final class Holder {

			private static final ToBytesTransformer SINGLETON = new ToBytesTransformer();
		}
	}

	/**
	 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
	 */
	private static final class Holder {

		private static final UlidJavaType SINGLETON = new UlidJavaType();
	}
}
