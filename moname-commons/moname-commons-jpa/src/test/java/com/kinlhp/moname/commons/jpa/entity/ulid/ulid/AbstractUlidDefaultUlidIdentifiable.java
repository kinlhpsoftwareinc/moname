package com.kinlhp.moname.commons.jpa.entity.ulid.ulid;

import java.io.Serial;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.github.f4b6a3.ulid.Ulid;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.jspecify.annotations.Nullable;

import com.kinlhp.moname.commons.jpa.annotations.UlidGenerator;
import com.kinlhp.moname.commons.jpa.entity.ulid.Discriminator;

import static com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable.DISCRIMINATOR_COLUMN;
import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidDefaultUlidIdentifiable.ENTITY;
import static com.kinlhp.moname.commons.jpa.entity.ulid.ulid.AbstractUlidUlidIdentifiable.TABLE;
import static jakarta.persistence.EnumType.STRING;

@AttributeOverride(column = @Column(name = "id"), name = "pk")
@DiscriminatorColumn(name = DISCRIMINATOR_COLUMN)
@Entity(name = ENTITY)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = TABLE)
public abstract class AbstractUlidDefaultUlidIdentifiable extends AbstractUlidUlidIdentifiable {

	@Serial
	private static final long serialVersionUID = -7174574983401479985L;

	public static final String DISCRIMINATOR_VALUE = "ULID_DEFAULT";

	public static final String ENTITY = "AbstractUlidDefaultUlidIdentifiable";

	public AbstractUlidDefaultUlidIdentifiable(final Ulid pk) {
		super(pk);
	}

	@Id
	@NotNull
	@Override
	@ToString.Include(rank = 1)
	@UlidGenerator
	public @Nullable Ulid getPk() {
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
