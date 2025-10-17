package com.kinlhp.moname.commons.api.openapi.mapper;

import java.io.Serializable;

import org.openapitools.jackson.nullable.JsonNullable;

public interface JsonNullableMapper<T extends Serializable, R extends Serializable> {

	JsonNullable<R> map(final T value);
}
