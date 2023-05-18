package com.kinlhp.moname.commons.test.jsr380;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

// TODO: Remove Lombok.
@Builder(access = AccessLevel.PACKAGE)
@Data
@EqualsAndHashCode
final class Bean implements Serializable {

	@Serial
	private static final long serialVersionUID = -2330247942546540406L;

	@Max(value = 0L)
	@NotNull
	@PositiveOrZero
	private Long bar;

	@NotBlank
	@Size(max = 1)
	private String foo;
}
