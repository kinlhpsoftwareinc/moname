package com.kinlhp.moname.commons.jpa.entity.ulid.bytes;

import java.io.Serial;

import jakarta.annotation.Nonnull;

import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable;

@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
public abstract class AbstractBytesUlidIdentifiable extends AbstractUlidIdentifiable<byte[]> {

	@Serial
	private static final long serialVersionUID = -7450302052447689680L;

	@Nonnull
	protected static final String TABLE = "bytes_ulid_identifiable";

	protected AbstractBytesUlidIdentifiable(@Nonnull final byte[] pk) {
		super(pk);
	}

	/**
	 * @deprecated {@link lombok.experimental.SuperBuilder @SuperBuilder} cannot handle the generic {@code byte[]} type,
	 * resulting in {@literal "java: wrong number of type arguments; required 3"} at compile time.
	 */
	@Deprecated(forRemoval = true, since = "1.0.0.BUILD-SNAPSHOT")
	@Generated
	public abstract static class AbstractBytesUlidIdentifiableBuilder<C extends AbstractBytesUlidIdentifiable,
			B extends AbstractBytesUlidIdentifiableBuilder<C, B>>
			extends AbstractUlidIdentifiable.AbstractUlidIdentifiableBuilder<byte[], C, B> {
	}
}
