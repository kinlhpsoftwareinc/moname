package com.kinlhp.moname.commons.jpa.entity.ulid.uuid;

import java.io.Serial;
import java.util.UUID;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.kinlhp.moname.commons.jpa.entity.ulid.AbstractUlidIdentifiable;

@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
public abstract class AbstractUuidUlidIdentifiable extends AbstractUlidIdentifiable<UUID> {

	@Serial
	private static final long serialVersionUID = -3545253572412724413L;

	protected static final String TABLE = "uuid_ulid_identifiable";

	protected AbstractUuidUlidIdentifiable(final UUID pk) {
		super(pk);
	}
}
