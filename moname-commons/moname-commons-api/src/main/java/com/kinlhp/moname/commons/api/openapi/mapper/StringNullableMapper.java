package com.kinlhp.moname.commons.api.openapi.mapper;

import org.mapstruct.Mapper;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper
public interface StringNullableMapper extends JsonNullableMapper<String, String> {

	@Override
	default JsonNullable<String> map(final String value) {
		return JsonNullable.of(value);
	}
}
