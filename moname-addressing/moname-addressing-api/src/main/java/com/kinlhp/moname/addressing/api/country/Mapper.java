package com.kinlhp.moname.addressing.api.country;

import java.util.Set;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.kinlhp.moname.addressing.api.oas.payload.Country;
import com.kinlhp.moname.addressing.domain.country.Entity;
import com.kinlhp.moname.commons.api.openapi.mapper.StringsNullableMapper;

@org.mapstruct.Mapper(uses = StringsNullableMapper.class)
public interface Mapper /*extends SliceFlatMapper<Entity, Country>*/ {

	@Nonnull
	Mapper INSTANCE = Mappers.getMapper(Mapper.class);

	@Mapping(target = "internetCctld", ignore = true)
	@Nullable
	Country map(@Nullable final Entity country);

	@Nullable
	Set<Country> map(@Nullable final Page<Entity> page);

	@org.mapstruct.Mapper
	interface Jpa {

		@Nonnull
		Jpa INSTANCE = Mappers.getMapper(Jpa.class);

		@Nonnull
		default Entity map(@Nonnull final com.kinlhp.moname.addressing.jpa.country.Entity country) {
			return Entity.of(country.getNumericCode());
		}

		@Nonnull
		default Page<Entity> map(@Nonnull final Page<com.kinlhp.moname.addressing.jpa.country.Entity> page) {
			return page.map(this::map);
		}
	}
}
