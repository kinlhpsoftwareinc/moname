package com.kinlhp.moname.commons.jpa.it.ulid.bytes;

import com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable;
import com.kinlhp.moname.commons.jpa.it.ulid.AbstractUlidGeneratorIT;

/**
 * Tests for Byte array ULID identifiable entities.
 */
abstract class AbstractBytesUlidGeneratorIT<T extends AbstractUlidIdentifiable<byte[]>>
		extends AbstractUlidGeneratorIT<T, byte[]> {
}
