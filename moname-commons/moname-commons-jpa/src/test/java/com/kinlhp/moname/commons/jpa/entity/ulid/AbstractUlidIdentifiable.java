package com.kinlhp.moname.commons.jpa.entity.ulid;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Access;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Transient;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator;
import com.kinlhp.moname.commons.jpa.entity.AbstractPersistable;

import static jakarta.persistence.AccessType.PROPERTY;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;

@Inheritance(strategy = SINGLE_TABLE)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@SuppressWarnings("java:S119")
@ToString(onlyExplicitlyIncluded = true)
public abstract class AbstractUlidIdentifiable<PK extends Serializable> extends AbstractPersistable<PK>
		implements Discriminable<Discriminator> {

	@Serial
	private static final long serialVersionUID = -9114914431327178587L;

	@Nonnull
	private static final String ANNOTATED_METHOD = "getPk";

	@Nonnull
	public static final String DISCRIMINATOR_COLUMN = "discriminator";

	@Access(PROPERTY)
	@Nullable
	@ToString.Include
	private Discriminator discriminator;

	protected AbstractUlidIdentifiable(@Nonnull final PK pk) {
		super(pk);
	}

	@Nonnull
	@Transient
	public Optional<UlidGenerator> getConfig() {
		try {
			@Nonnull final var config = getClass().getMethod(ANNOTATED_METHOD).getAnnotation(UlidGenerator.class);
			return Optional.ofNullable(config);
		} catch (@Nonnull final NoSuchMethodException exception) {
			throw new IllegalStateException(exception);
		}
	}

	/**
	 * Alternative to bypass the access strategy behavior, which by default does not allow this abstract class to have
	 * an accessor method for the {@code discriminator} attribute and to which the access strategy is defined in the
	 * concrete implementations.
	 *
	 * @see Discriminable#getDiscriminator() getDiscriminator
	 */
	@Nullable
	@Transient
	protected Discriminator getDiscriminatorValue() {
		return discriminator;
	}

	@Override
	public void setDiscriminator(@Nullable final Discriminator discriminator) {
		this.discriminator = discriminator;
	}
}
