package com.kinlhp.moname.commons.jpa.entity;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Read-only test entity.
 */
@AttributeOverride(column = @Column(name = "id"), name = "pk")
@Entity
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "read_only") // TODO: https://stackoverflow.com/questions/49148172/table-name-configured-with-external-properties-file
@ToString(onlyExplicitlyIncluded = true)
public class ReadOnly extends AbstractReadOnly<Integer> {

	@Serial
	private static final long serialVersionUID = 5159603689855870011L;

	@Getter
	@NotBlank
	@Setter
	@Size(max = 1)
	@ToString.Include
	private char description;

	public ReadOnly(@Nonnull final Integer id, final char description) {
		super(id);
		this.description = description;
	}

	@Id
	@Max(255L)
	@Min(1L)
	@Nonnull
	@NotNull
	@Override
	@ToString.Include(rank = 1)
	public Integer getPk() {
		return primaryKey();
	}
}
