package com.kinlhp.moname.commons.jpa.entity.ulid.uuid;

import java.io.Serial;
import java.util.UUID;

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
import org.jspecify.annotations.Nullable;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator;
import com.kinlhp.moname.commons.jpa.entity.ulid.Discriminator;

import static com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style.AUTO;
import static com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable.DISCRIMINATOR_COLUMN;
import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidAutoUlidIdentifiable.ENTITY;
import static com.kinlhp.moname.commons.jpa.entity.ulid.uuid.AbstractUuidUlidIdentifiable.TABLE;
import static jakarta.persistence.EnumType.STRING;

@AttributeOverride(column = @Column(name = "id"), name = "pk")
@DiscriminatorColumn(name = DISCRIMINATOR_COLUMN)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = TABLE)
public abstract class AbstractUuidAutoUlidIdentifiable extends AbstractUuidUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -6550026196210779930L;

	public static final String DISCRIMINATOR_VALUE = "UUID_AUTO";

	public static final String ENTITY = "AbstractUuidAutoUlidIdentifiable";

	public AbstractUuidAutoUlidIdentifiable(final UUID pk) {
		super(pk);
	}

	@Id
	@NotNull
	@Override
	@ToString.Include(rank = 1)
	@UlidGenerator(style = AUTO)
	public @Nullable UUID getPk() {
		return getPrimaryKey();
	}

	@Column(insertable = false, name = DISCRIMINATOR_COLUMN, updatable = false)
	@Enumerated(STRING)
	@Override
	@ToString.Include
	public @Nullable Discriminator getDiscriminator() {
		return getDiscriminatorValue();
	}
}
