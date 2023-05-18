package com.kinlhp.moname.commons.jpa.entity;

import java.io.Serial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import com.kinlhp.moname.commons.jpa.AbstractReadOnly;

/**
 * Read-only test entity.
 */
@AllArgsConstructor
@AttributeOverride(column = @Column(name = "id"), name = "pk")
@Builder
@Entity
//@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
//@NoArgsConstructor(onConstructor_ = {@Nonnull})
//@SuperBuilder(toBuilder = true)
@Table(name = "read_only")
//@ToString(callSuper = true)
public class ReadOnlyEntity extends AbstractReadOnly<Integer> {

	@Serial
	private static final long serialVersionUID = -7632774257865723990L;

	@Getter(onMethod_ = {@Nonnull})
	@NotBlank
	@Setter(onParam_ = {@Nonnull})
	@Size(max = 1)
	private char description;

	public ReadOnlyEntity() {
	}

	@Max(value = 255L)
	@Min(value = 1L)
	@Nonnull
	@NotNull
	@Override
	public Integer getPK() {
		return super.getPK();
	}

	@Override
	public void setPK(@Max(value = 255L) @Min(value = 1L) @Nonnull @NotNull final Integer pk) {
		super.setPK(pk);
	}
}
