package com.kinlhp.moname.commons.jpa.entity.ulid.string;

import java.io.Serial;

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
import org.jspecify.annotations.Nullable;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator;
import com.kinlhp.moname.commons.jpa.entity.ulid.Discriminator;

import static com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable.DISCRIMINATOR_COLUMN;
import static com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringDefaultUlidIdentifiable.ENTITY;
import static com.kinlhp.moname.commons.jpa.entity.ulid.string.AbstractStringUlidIdentifiable.TABLE;
import static jakarta.persistence.EnumType.STRING;

@AttributeOverride(column = @Column(name = "id"), name = "pk")
@DiscriminatorColumn(name = DISCRIMINATOR_COLUMN)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = TABLE)
public abstract class AbstractStringDefaultUlidIdentifiable extends AbstractStringUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = 2768014701819785117L;

	public static final String DISCRIMINATOR_VALUE = "STRING_DEFAULT";

	public static final String ENTITY = "AbstractStringDefaultUlidIdentifiable";

	public AbstractStringDefaultUlidIdentifiable(final String pk) {
		super(pk);
	}

	@Id
	@NotNull
	@Override
	@Size(max = 26, min = 26)
	@ToString.Include(rank = 1)
	@UlidGenerator
	public @Nullable String getPk() {
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
