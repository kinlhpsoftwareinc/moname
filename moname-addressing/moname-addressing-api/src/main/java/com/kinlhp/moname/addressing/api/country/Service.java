package com.kinlhp.moname.addressing.api.country;

import jakarta.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kinlhp.moname.addressing.domain.country.Entity;

@org.springframework.stereotype.Service
@SuppressWarnings("java:S2176")
public class Service extends com.kinlhp.moname.addressing.domain.country.Service {

	@Nonnull
	private final Mapper.Jpa mapper = Mapper.Jpa.INSTANCE;

	public Service(@Autowired @Nonnull Repository repository) {
		super(repository);
	}

	@Nonnull
	public Page<Entity> getAll(@Nonnull final Pageable pageable) {
		@Nonnull final var page = getPage(pageable);
		return mapper.map(page);
		//getAll() // TODO: Fazer com que esse metodo seja chamado.
	}

	@Nonnull
	private Page<com.kinlhp.moname.addressing.jpa.country.Entity> getPage(@Nonnull final Pageable pageable) {
		// TODO: Log: buscando a pagina XYZ no repositorio
		return ((Repository) getCountries()).findAll(pageable);
	}
}
