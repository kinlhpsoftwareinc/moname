package com.kinlhp.moname.commons.jpa.converter;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter(autoApply = true)
public class StringArrayConverter implements AttributeConverter<String[], String> {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(StringArrayConverter.class);

	@Nonnull
	private static final String DELIMITING_REGULAR_EXPRESSION = ",";

	@Nullable
	@Override
	public String convertToDatabaseColumn(@Nonnull final String[] attribute) {
		LOG.trace("Converting the entity attribute {} into the database value", (Object) attribute);
		return attribute.length != 0
				? String.join(DELIMITING_REGULAR_EXPRESSION, attribute)
				: null;
	}

	@Nonnull
	@Override
	public String[] convertToEntityAttribute(@Nullable final String dbData) {
		LOG.trace("Converting the database value {} into the entity attribute", dbData);
		return dbData != null
				? dbData.split(DELIMITING_REGULAR_EXPRESSION)
				: new String[0];
	}
}
