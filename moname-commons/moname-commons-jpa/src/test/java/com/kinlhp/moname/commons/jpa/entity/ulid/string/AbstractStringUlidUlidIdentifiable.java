package com.kinlhp.moname.commons.jpa.entity.ulid.string;

import java.io.Serial;

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
import jakarta.validation.constraints.Size;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator;
import com.kinlhp.moname.commons.jpa.entity.ulid.Discriminator;

import static com.kinlhp.moname.commons.jpa.annotations.UlidGenerator.Style.ULID;
import static com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable.DISCRIMINATOR_COLUMN;
import static com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringUlidIdentifiable.TABLE;
import static com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringUlidUlidIdentifiable.ENTITY;
import static jakarta.persistence.EnumType.STRING;

@AttributeOverride(column = @Column(name = "id"), name = "pk")
@DiscriminatorColumn(name = DISCRIMINATOR_COLUMN)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = TABLE)
public abstract class AbstractStringUlidUlidIdentifiable extends AbstractStringUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = 8873270945327060068L;

	@Nonnull
	public static final String DISCRIMINATOR_VALUE = "STRING_ULID";

	@Nonnull
	public static final String ENTITY = "AbstractStringUlidUlidIdentifiable";

	public AbstractStringUlidUlidIdentifiable(@Nonnull final String pk) {
		super(pk);
	}

	@Id
	@NotNull
	@Nullable
	@Override
	@Size(max = 26, min = 26)
	@ToString.Include(rank = 1)
	@UlidGenerator(style = ULID)
	public String getPk() {
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
