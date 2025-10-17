package com.kinlhp.moname.commons.jpa.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import com.kinlhp.moname.commons.jpa.listener.ReadOnlyListener;

/**
 * Abstract implementation for read-only entities.
 */
@EntityListeners(ReadOnlyListener.class)
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@SuppressWarnings("java:S119")
@ToString(onlyExplicitlyIncluded = true)
public abstract class AbstractReadOnly<PK extends Serializable> implements Readable<PK> {

	@Serial
	private static final long serialVersionUID = 7562415558962410287L;

	@Access(AccessType.PROPERTY)
	@ToString.Include
	private PK pk;

	protected AbstractReadOnly(@Nonnull final PK pk) {
		this();
		this.pk = pk;
	}

	/**
	 * Alternative to bypass the access strategy behavior, which by default does
	 * not allow this abstract class to have an accessor method for the PK
	 * attribute and to which the access strategy is defined in the concrete
	 * implementations.
	 */
	@Nonnull
	protected PK primaryKey() {
		return pk;
	}

	public void setPk(@Nonnull final PK pk) {
		this.pk = pk;
	}

	@Override
	@SuppressWarnings("java:S2097")
	public final boolean equals(@Nullable final Object other) {
		if (this == other) return true;
		if (other == null) return false;
		@Nonnull final var thisEffectiveClass = effectiveClassOf(this);
		@Nonnull final var thatEffectiveClass = effectiveClassOf(other);
		if (thisEffectiveClass != thatEffectiveClass) return false;
		@Nonnull final var that = (AbstractReadOnly<?>) other;
		return Objects.equals(getPk(), that.getPk());
	}

	@Override
	public final int hashCode() {
		return effectiveClassOf(this).hashCode();
	}

	@Nonnull
	private Class<?> effectiveClassOf(@Nonnull final Object object) {
		return object instanceof @Nonnull final HibernateProxy hibernateProxy
				? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
				: object.getClass();
	}
}
