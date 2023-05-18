package com.kinlhp.moname.addressing.api.country;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.kinlhp.moname.addressing.api.oas.payload.CountriesResponse;
import com.kinlhp.moname.addressing.api.oas.payload.CountryResponse;
import com.kinlhp.moname.addressing.domain.country.Entity;
import com.kinlhp.moname.commons.api.openapi.mapper.StringNullableMapper;

@org.mapstruct.Mapper(uses = {StringNullableMapper.class})
public interface Mapper {

	Mapper INSTANCE = Mappers.getMapper(Mapper.class);

	CountryResponse map(Entity country);

	@Mapping(source = "content", target = "countries")
	CountriesResponse map(Page<Entity> page);
}
