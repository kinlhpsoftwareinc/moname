package com.kinlhp.moname.commons.jpa.entity.ulid.uuid;

import java.io.Serial;
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
import jakarta.validation.constraints.NotNull;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator;
import com.kinlhp.moname.commons.jpa.entity.ulid.Discriminator;

import static com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable.DISCRIMINATOR_COLUMN;
import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidDefaultUlidIdentifiable.ENTITY;
import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidUlidIdentifiable.TABLE;
import static jakarta.persistence.EnumType.STRING;

@AttributeOverride(column = @Column(name = "id"), name = "pk")
@DiscriminatorColumn(name = DISCRIMINATOR_COLUMN)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = TABLE)
public abstract class AbstractUuidDefaultUlidIdentifiable extends AbstractUuidUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = 8008124784379942771L;

	@Nonnull
	public static final String DISCRIMINATOR_VALUE = "UUID_DEFAULT";

	@Nonnull
	public static final String ENTITY = "AbstractUuidDefaultUlidIdentifiable";

	public AbstractUuidDefaultUlidIdentifiable(@Nonnull final UUID pk) {
		super(pk);
	}

	@Id
	@NotNull
	@Nullable
	@Override
	@ToString.Include(rank = 1)
	@UlidGenerator
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
}
