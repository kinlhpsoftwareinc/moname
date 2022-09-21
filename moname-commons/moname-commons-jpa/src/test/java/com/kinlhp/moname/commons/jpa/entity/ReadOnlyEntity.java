package com.kinlhp.moname.commons.jpa.entity;

import java.io.Serial;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.AbstractReadOnly;

/**
 * Read-only test entity.
 */
@AttributeOverride(column = @Column(name = "id"), name = "pk")
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "read_only")
@ToString(callSuper = true)
public class ReadOnlyEntity extends AbstractReadOnly<Integer> {

	@Serial
	private static final long serialVersionUID = -4943378731460389060L;

	@Getter
	@NotBlank
	@Setter(onParam = @__({ @NotNull }))
	private char description;

	@Max(value = 255L)
	@Min(value = 1L)
	@Override
	public Integer getPk() {
		return super.getPk();
	}

}
