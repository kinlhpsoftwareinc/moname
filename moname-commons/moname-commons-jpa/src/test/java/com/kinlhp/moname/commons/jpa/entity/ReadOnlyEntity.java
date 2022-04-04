package com.kinlhp.moname.commons.jpa.entity;

import java.io.Serial;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
	private static final long serialVersionUID = 8231703272479099670L;

	@Getter
	@NotBlank
	@Setter(onParam = @__({ @NotNull }))
	private char description;

}
