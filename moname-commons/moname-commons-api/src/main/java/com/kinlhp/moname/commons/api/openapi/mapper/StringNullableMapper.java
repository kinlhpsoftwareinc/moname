package com.kinlhp.moname.commons.api.openapi.mapper;

import jakarta.annotation.Nonnull;

import org.mapstruct.Mapper;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper
public interface StringNullableMapper extends JsonNullableMapper<String> {

	@Nonnull
	@Override
	default JsonNullable<String> map(@Nonnull final String value) {
		return JsonNullable.of(value);
	}
}
