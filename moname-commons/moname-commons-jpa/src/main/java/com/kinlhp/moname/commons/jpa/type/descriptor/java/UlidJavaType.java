package com.kinlhp.moname.commons.jpa.type.descriptor.java;

import com.github.f4b6a3.ulid.Ulid;
import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(UlidJavaType.class);

	@Nonnull
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

	@Nonnull
	@Override
	public String toString(@Nonnull final Ulid value) {
		return ToStringTransformer.getSingleton().transform(value);
	}

	@Nonnull
	@Override
	public Ulid fromString(@Nonnull final CharSequence string) {
		return ToStringTransformer.getSingleton().parse(string);
	}

	@Override
	public long getDefaultSqlLength(@Nonnull final Dialect dialect, @Nonnull final JdbcType jdbcType) {
		return switch (jdbcType.getDdlTypeCode()) {
			case ARRAY, BINARY -> 16;
			case VARCHAR -> 26;
			default -> super.getDefaultSqlLength(dialect, jdbcType);
		};
	}

	@Nullable
	@Override
	public <T> T unwrap(@Nullable final Ulid value, @Nonnull final Class<T> type,
			@Nonnull final WrapperOptions options) {
		@Nonnull final var message = "Value of type %s will be unwrapped to {} value of type {}"
				.formatted(Ulid.class.getName());
		return value == null
				? null
				: switch (type) {
			case @Nonnull final Class<T> t when Ulid.class.isAssignableFrom(t) -> {
				LOG.trace(message, "ULID", t.getName());
				//noinspection unchecked
				yield (T) PassThroughTransformer.getSingleton().transform(value);
			}
			case @Nonnull final Class<T> t when UUID.class.isAssignableFrom(t) -> {
				LOG.trace(message, "UUID", t.getName());
				//noinspection unchecked
				yield (T) ToUuidTransformer.getSingleton().transform(value);
			}
			case @Nonnull final Class<T> t when String.class.isAssignableFrom(t) -> {
				LOG.trace(message, "string", t.getName());
				//noinspection unchecked
				yield (T) ToStringTransformer.getSingleton().transform(value);
			}
			case @Nonnull final Class<T> t when byte[].class.isAssignableFrom(t) -> {
				LOG.trace(message, "byte array", t.getName());
				yield (T) ToBytesTransformer.getSingleton().transform(value);
			}
			default -> throw unknownUnwrap(type);
		};
	}

	@Nonnull
	@Override
	public HibernateException unknownUnwrap(@Nonnull final Class<?> conversionType) {
		return super.unknownUnwrap(conversionType);
	}

	@Nullable
	@Override
	@SuppressWarnings("java:S2259")
	public <X> Ulid wrap(@Nullable final X value, @Nullable final WrapperOptions options) {
		@Nonnull final var message = "{} value of type {} will be wrapped to value of type %s"
				.formatted(Ulid.class.getName());
		return switch (value) {
			case null -> null;
			case @Nonnull final Ulid ulid -> {
				LOG.trace(message, "ULID", ulid.getClass().getName());
				yield PassThroughTransformer.getSingleton().parse(ulid);
			}
			case @Nonnull final UUID uuid -> {
				LOG.trace(message, "UUID", uuid.getClass().getName());
				yield ToUuidTransformer.getSingleton().parse(uuid);
			}
			case @Nonnull final String string -> {
				LOG.trace(message, "string", string.getClass().getName());
				yield ToStringTransformer.getSingleton().parse(string);
			}
			case @Nonnull final byte[] bytes -> {
				LOG.trace(message, "byte array", bytes.getClass().getName());
				yield ToBytesTransformer.getSingleton().parse(bytes);
			}
			default -> throw unknownWrap(value.getClass());
		};
	}

	@Nonnull
	@Override
	protected HibernateException unknownWrap(@Nonnull final Class<?> conversionType) {
		return super.unknownWrap(conversionType);
	}

	public interface ValueTransformer {

		@Nonnull
		Serializable transform(@Nonnull final Ulid ulid);

		@Nonnull
		@SuppressWarnings("unused")
		Ulid parse(@Nonnull final Object value);
	}

	public static class PassThroughTransformer implements ValueTransformer {

		@Nonnull
		public static PassThroughTransformer getSingleton() {
			return Holder.SINGLETON;
		}

		private PassThroughTransformer() {
		}

		@Nonnull
		@Override
		public Ulid transform(@Nonnull final Ulid ulid) {
			return ulid;
		}

		@Nonnull
		@Override
		public Ulid parse(@Nonnull final Object value) {
			return (Ulid) value;
		}

		/**
		 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
		 */
		private static final class Holder {

			@Nonnull
			private static final PassThroughTransformer SINGLETON = new PassThroughTransformer();
		}
	}

	public static class ToUuidTransformer implements ValueTransformer {

		@Nonnull
		public static ToUuidTransformer getSingleton() {
			return Holder.SINGLETON;
		}

		private ToUuidTransformer() {
		}

		@Nonnull
		@Override
		public UUID transform(@Nonnull final Ulid ulid) {
			return ulid.toUuid();
		}

		@Nonnull
		@Override
		public Ulid parse(@Nonnull final Object value) {
			return Ulid.from((UUID) value);
		}

		/**
		 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
		 */
		private static final class Holder {

			@Nonnull
			private static final ToUuidTransformer SINGLETON = new ToUuidTransformer();
		}
	}

	public static class ToStringTransformer implements ValueTransformer {

		@Nonnull
		public static ToStringTransformer getSingleton() {
			return Holder.SINGLETON;
		}

		private ToStringTransformer() {
		}

		@Nonnull
		@Override
		public String transform(@Nonnull final Ulid ulid) {
			return ulid.toString();
		}

		@Nonnull
		@Override
		public Ulid parse(@Nonnull final Object value) {
			return Ulid.from(value.toString());
		}

		/**
		 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
		 */
		private static final class Holder {

			@Nonnull
			private static final ToStringTransformer SINGLETON = new ToStringTransformer();
		}
	}

	public static class ToBytesTransformer implements ValueTransformer {

		@Nonnull
		public static ToBytesTransformer getSingleton() {
			return Holder.SINGLETON;
		}

		private ToBytesTransformer() {
		}

		@Nonnull
		@Override
		public byte[] transform(@Nonnull final Ulid ulid) {
			return ulid.toBytes();
		}

		@Nonnull
		@Override
		public Ulid parse(@Nonnull final Object value) {
			return Ulid.from((byte[]) value);
		}

		/**
		 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
		 */
		private static final class Holder {

			@Nonnull
			private static final ToBytesTransformer SINGLETON = new ToBytesTransformer();
		}
	}

	/**
	 * @implSpec The initialization-on-demand holder idiom (also known as the Bill Pugh Singleton).
	 */
	private static final class Holder {

		@Nonnull
		private static final UlidJavaType SINGLETON = new UlidJavaType();
	}
}
