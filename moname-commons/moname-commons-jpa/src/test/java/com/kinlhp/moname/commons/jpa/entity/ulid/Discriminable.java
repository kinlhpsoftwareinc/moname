package com.kinlhp.moname.commons.jpa.entity.ulid;

import org.jspecify.annotations.Nullable;

import java.io.Serializable;

public interface Discriminable<T extends Serializable> {

	@Nullable T getDiscriminator();

	void setDiscriminator(final T discriminator);
}
