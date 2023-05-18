package com.kinlhp.moname.addressing.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.kinlhp.moname.addressing.api.country.Endpoint;
import com.kinlhp.moname.addressing.api.oas.endpoint.CountriesApiDelegate;
import com.kinlhp.moname.addressing.domain.country.Entity;
import com.kinlhp.moname.addressing.domain.country.Repository;

@EnableJpaRepositories(basePackageClasses = {Repository.class})
@EntityScan(basePackageClasses = {Entity.class})
@SpringBootApplication(scanBasePackageClasses = {
	CountriesApiDelegate.class,
	Endpoint.class
})
public class Application {

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
