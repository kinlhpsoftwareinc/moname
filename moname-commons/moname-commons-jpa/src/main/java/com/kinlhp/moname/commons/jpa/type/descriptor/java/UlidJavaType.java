package com.kinlhp.moname.commons.jpa.type.descriptor.java;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import com.github.f4b6a3.ulid.Ulid;
import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	@SuppressWarnings("java:S3077")
	private static volatile UlidJavaType singleton = null;

	@Nonnull
	public static UlidJavaType getSingleton() {
		if (singleton == null) {
			synchronized (UlidJavaType.class) {
				if (singleton == null) {
					singleton = new UlidJavaType();
				}
			}
		}
		return singleton;
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

		@SuppressWarnings("java:S3077")
		private static volatile PassThroughTransformer singleton = null;

		@Nonnull
		public static PassThroughTransformer getSingleton() {
			if (singleton == null) {
				synchronized (PassThroughTransformer.class) {
					if (singleton == null) {
						singleton = new PassThroughTransformer();
					}
				}
			}
			return singleton;
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
	}

	public static class ToUuidTransformer implements ValueTransformer {

		@SuppressWarnings("java:S3077")
		private static volatile ToUuidTransformer singleton = null;

		@Nonnull
		public static ToUuidTransformer getSingleton() {
			if (singleton == null) {
				synchronized (ToUuidTransformer.class) {
					if (singleton == null) {
						singleton = new ToUuidTransformer();
					}
				}
			}
			return singleton;
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
	}

	public static class ToStringTransformer implements ValueTransformer {

		@SuppressWarnings("java:S3077")
		private static volatile ToStringTransformer singleton = null;

		@Nonnull
		public static ToStringTransformer getSingleton() {
			if (singleton == null) {
				synchronized (ToStringTransformer.class) {
					if (singleton == null) {
						singleton = new ToStringTransformer();
					}
				}
			}
			return singleton;
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
	}

	public static class ToBytesTransformer implements ValueTransformer {

		@SuppressWarnings("java:S3077")
		private static volatile ToBytesTransformer singleton = null;

		@Nonnull
		public static ToBytesTransformer getSingleton() {
			if (singleton == null) {
				synchronized (ToBytesTransformer.class) {
					if (singleton == null) {
						singleton = new ToBytesTransformer();
					}
				}
			}
			return singleton;
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
	}
}
