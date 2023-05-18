package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Abstract implementation for identifiable entities.
 */
//@EqualsAndHashCode
//@RequiredArgsConstructor(onConstructor_ = {@Nonnull})
//@SuperBuilder(toBuilder = true)
public abstract class AbstractIdentifiable<SELF extends AbstractIdentifiable<SELF, ID>, ID extends Serializable> implements Identifiable<ID> {

	@Serial
	private static final long serialVersionUID = 133670460603901937L;

	@Nonnull
	private ID identity;

	AbstractIdentifiable(@Nonnull final ID identity) {
		this.identity = identity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Nonnull
	@Override
	public ID getIdentity() {
		return identity;
	}

	/**
	 * Creates and returns a copy of {@code this}, identified by the given identity (ID).
	 *
	 * @param newIdentity the new identity (ID) for the copy entity.
	 * @return a copy of {@code this}, identified by the given identity (ID).
	 * @see Object#clone()
	 */
	@Nonnull
	public final SELF copyIdentifiedBy(@Nonnull final ID newIdentity) {
		try {
			@SuppressWarnings("unchecked") @Nonnull final var copy = (SELF) clone();
			return identify().apply(copy, newIdentity);
		} catch (@Nonnull final CloneNotSupportedException exception) {
			@Nonnull final var message = String.format("The domain entity identified by %s cannot in any way be identified by %s.", getIdentity(), newIdentity);
			throw new UnsupportedOperationException(message, exception);
		}
	}

	@Nonnull
	protected BiFunction<SELF, ID, SELF> identify() {
		return AbstractIdentifiable::setIdentity;
	}

	@Nonnull
	final SELF setIdentity(@Nonnull final ID newIdentity) {
		identity = newIdentity;
		//noinspection unchecked
		return (SELF) this;
	}

	@Override
	public boolean equals(@Nullable final Object object) {
		if (this == object) return true;
		if (!(object instanceof @Nonnull final AbstractIdentifiable<?, ?> that)) return false;
		return getClass().equals(that.getClass()) && Objects.equals(getIdentity(), that.getIdentity());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIdentity());
	}
}
