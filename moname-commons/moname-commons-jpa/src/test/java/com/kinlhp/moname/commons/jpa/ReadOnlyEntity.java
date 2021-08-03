package com.kinlhp.moname.commons.jpa;

import java.io.Serial;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Read-only test entity.
 */
@AttributeOverride(column = @Column(name = "id"), name = "pk")
@Data
@Entity(name = "read_only")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReadOnlyEntity extends AbstractReadOnly<Integer> {

	@Serial
	private static final long serialVersionUID = 3444285990341475303L;

	private char description;

}
