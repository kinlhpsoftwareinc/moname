package com.kinlhp.moname.commons.test;

import java.io.Serial;
import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder(access = AccessLevel.PACKAGE)
@Data
@EqualsAndHashCode
final class Bean implements Serializable {

	@Serial
	private static final long serialVersionUID = 3051166050701089093L;

	@Max(value = 0L)
	@NotNull
	@PositiveOrZero
	private Long bar;

	@NotBlank
	@Size(max = 1)
	private String foo;

}
