package com.kinlhp.moname.commons.jpa.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

/**
 * Abstract implementation for persistable entities.
 */
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@SuppressWarnings("java:S119")
@ToString(onlyExplicitlyIncluded = true)
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

	@Serial
	private static final long serialVersionUID = 2481456990590890815L;

	@Access(AccessType.PROPERTY)
	@Nullable
	@ToString.Include
	private PK pk;

	protected AbstractPersistable(@Nonnull final PK pk) {
		this.pk = pk;
	}

	/**
	 * Alternative to bypass the access strategy behavior, which by default does
	 * not allow this abstract class to have an accessor method for the
	 * {@code pk} attribute and to which the access strategy is defined in the
	 * concrete implementations.
	 *
	 * @see Persistable#getPk() getPk
	 */
	@Nullable
	@Transient
	protected PK getPrimaryKey() {
		return pk;
	}

	@Override
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
		@Nonnull final var that = (AbstractPersistable<?>) other;
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
