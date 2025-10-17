package com.kinlhp.moname.commons.jpa.entity.ulid.ulid;

import java.io.Serial;
import java.util.Optional;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import com.github.f4b6a3.ulid.Ulid;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator;
import com.kinlhp.moname.commons.jpa.entity.ulid.Discriminator;
import com.kinlhp.moname.commons.jpa.id.ulid.HashableUlid;

import static com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style.HASH;
import static com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable.DISCRIMINATOR_COLUMN;
import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidHashUlidIdentifiable.ENTITY;
import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidUlidIdentifiable.TABLE;
import static jakarta.persistence.EnumType.STRING;

@AttributeOverride(column = @Column(name = "id"), name = "pk")
@DiscriminatorColumn(name = DISCRIMINATOR_COLUMN)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = TABLE)
public abstract class AbstractUlidHashUlidIdentifiable extends AbstractUlidUlidIdentifiable implements HashableUlid {

	@Serial
	private static final long serialVersionUID = 2988795128303801643L;

	@Nonnull
	public static final String DISCRIMINATOR_VALUE = "ULID_HASH";

	@Nonnull
	public static final String ENTITY = "AbstractUlidHashUlidIdentifiable";

	public AbstractUlidHashUlidIdentifiable(@Nonnull final Ulid pk) {
		super(pk);
	}

	@Id
	@NotNull
	@Nullable
	@Override
	@ToString.Include(rank = 1)
	@UlidGenerator(string = ENTITY, style = HASH)
	public Ulid getPk() {
		return getPrimaryKey();
	}

	@Column(insertable = false, name = DISCRIMINATOR_COLUMN, updatable = false)
	@Enumerated(STRING)
	@Nullable
	@Override
	@ToString.Include
	public Discriminator getDiscriminator() {
		return getDiscriminatorValue();
	}

	@Nonnull
	@Override
	@Transient
	public Optional<UlidGenerator> getHashConfig() {
		return super.getConfig();
	}
}
