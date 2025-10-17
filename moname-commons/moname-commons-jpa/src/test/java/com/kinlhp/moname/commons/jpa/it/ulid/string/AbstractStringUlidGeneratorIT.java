package com.kinlhp.moname.commons.jpa.it.ulid.string;

import com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.it.ulid.AbstractUlidGeneratorIT;

/**
 * Tests for String ULID identifiable entities.
 */
abstract class AbstractStringUlidGeneratorIT<T extends AbstractUlidIdentifiable<String>>
		extends AbstractUlidGeneratorIT<T, String> {
}
