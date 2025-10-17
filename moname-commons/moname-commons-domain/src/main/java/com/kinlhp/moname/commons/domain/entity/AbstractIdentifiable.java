package com.kinlhp.moname.commons.domain.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation for identifiable entities.
 */
@SuppressWarnings("java:S119")
public abstract class AbstractIdentifiable<SELF extends AbstractIdentifiable<SELF, ID>, ID extends Serializable>
		implements Identifiable<ID> {

	@Serial
	private static final long serialVersionUID = -8986527541223113323L;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractIdentifiable.class);

	private ID identity;

	AbstractIdentifiable(final ID identity) {
		this.identity = identity;
	}

	@Override
	public ID getIdentity() {
		return identity;
	}

	/**
	 * Creates and returns a copy of {@code this}, identified by the given identity (ID).
	 *
	 * @param newIdentity the new identity (ID) for the copy entity.
	 * @return a copy of {@code this}, identified by the given identity (ID).
	 * @see Object#clone() clone
	 */
	public final SELF copyIdentifiedBy(final ID newIdentity) {
		try {
			final @SuppressWarnings("unchecked") var copy = (SELF) clone();
			return identify().apply(copy, newIdentity);
		} catch (final @SuppressWarnings("java:S2139") CloneNotSupportedException exception) {
			final var message = "The domain entity identified by %s cannot in any way be identified by %s"
					.formatted(getIdentity(), newIdentity);
			LOG.error(message, exception);
			throw new UnsupportedOperationException(message, exception);
		}
	}

	protected BiFunction<SELF, ID, SELF> identify() {
		return AbstractIdentifiable::setIdentity;
	}

	private SELF setIdentity(final ID newIdentity) {
		identity = newIdentity;
		//noinspection unchecked
		return (SELF) this;
	}

	@Override
	public boolean equals(final @Nullable Object object) {
		if (this == object) return true;
		if (!(object instanceof final AbstractIdentifiable<?, ?> that)) return false;
		return getClass().equals(that.getClass()) && Objects.equals(getIdentity(), that.getIdentity());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIdentity());
	}
}
