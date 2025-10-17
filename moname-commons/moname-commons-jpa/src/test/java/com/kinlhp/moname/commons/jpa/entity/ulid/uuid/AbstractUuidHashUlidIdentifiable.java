package com.kinlhp.moname.commons.jpa.entity.ulid.uuid;

import java.io.Serial;
import java.util.Optional;
import java.util.UUID;

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

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator;
import com.kinlhp.moname.commons.jpa.entity.ulid.Discriminator;
import com.kinlhp.moname.commons.jpa.id.ulid.HashableUlid;

import static com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style.HASH;
import static com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable.DISCRIMINATOR_COLUMN;
import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidHashUlidIdentifiable.ENTITY;
import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidUlidIdentifiable.TABLE;
import static jakarta.persistence.EnumType.STRING;

@AttributeOverride(column = @Column(name = "id"), name = "pk")
@DiscriminatorColumn(name = DISCRIMINATOR_COLUMN)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = TABLE)
public abstract class AbstractUuidHashUlidIdentifiable extends AbstractUuidUlidIdentifiable implements HashableUlid {

	@Serial
	private static final long serialVersionUID = 7586259552970633048L;

	@Nonnull
	public static final String DISCRIMINATOR_VALUE = "UUID_HASH";

	@Nonnull
	public static final String ENTITY = "AbstractUuidHashUlidIdentifiable";

	public AbstractUuidHashUlidIdentifiable(@Nonnull final UUID pk) {
		super(pk);
	}

	@Id
	@NotNull
	@Nullable
	@Override
	@ToString.Include(rank = 1)
	@UlidGenerator(string = ENTITY, style = HASH)
	public UUID getPk() {
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
