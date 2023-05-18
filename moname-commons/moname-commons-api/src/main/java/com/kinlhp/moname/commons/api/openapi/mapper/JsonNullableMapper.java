package com.kinlhp.moname.commons.api.openapi.mapper;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import org.openapitools.jackson.nullable.JsonNullable;

public interface JsonNullableMapper<T extends Serializable> {

	@Nonnull
	JsonNullable<T> map(@Nonnull final T value);
}
