package com.kinlhp.moname.addressing.api.country;

import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.kinlhp.moname.addressing.api.oas.payload.Country;
import com.kinlhp.moname.addressing.domain.country.Entity;
import com.kinlhp.moname.commons.api.openapi.mapper.StringsNullableMapper;

@org.mapstruct.Mapper(uses = StringsNullableMapper.class)
public interface Mapper /*extends SliceFlatMapper<Entity, Country>*/ {

	Mapper INSTANCE = Mappers.getMapper(Mapper.class);

	@Mapping(target = "internetCctld", ignore = true)
	@Nullable Country map(final @Nullable Entity country);

	@Nullable Set<Country> map(final @Nullable Page<Entity> page);

	@org.mapstruct.Mapper
	interface Jpa {

		Jpa INSTANCE = Mappers.getMapper(Jpa.class);

		default Entity map(final com.kinlhp.moname.addressing.jpa.country.Entity country) {
			return Entity.of(country.getNumericCode());
		}

		default Page<Entity> map(final Page<com.kinlhp.moname.addressing.jpa.country.Entity> page) {
			return page.map(this::map);
		}
	}
}
