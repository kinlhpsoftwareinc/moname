package com.kinlhp.moname.commons.jpa.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter(autoApply = true)
public class StringArrayConverter implements AttributeConverter<String[], String> {

	private static final Logger LOG = LoggerFactory.getLogger(StringArrayConverter.class);

	private static final String DELIMITING_REGULAR_EXPRESSION = ",";

	@Override
	public @Nullable String convertToDatabaseColumn(final String[] attribute) {
		LOG.trace("Converting the entity attribute {} into the database value", (Object) attribute);
		return attribute.length != 0
				? String.join(DELIMITING_REGULAR_EXPRESSION, attribute)
				: null;
	}

	@Override
	public String[] convertToEntityAttribute(final @Nullable String dbData) {
		LOG.trace("Converting the database value {} into the entity attribute", dbData);
		return dbData != null
				? dbData.split(DELIMITING_REGULAR_EXPRESSION)
				: new String[0];
	}
}
