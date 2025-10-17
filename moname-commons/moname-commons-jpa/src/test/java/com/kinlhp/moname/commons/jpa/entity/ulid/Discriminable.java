package com.kinlhp.moname.commons.jpa.entity.ulid;

import java.io.Serializable;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface Discriminable<T extends Serializable> {

	@Nullable
	T getDiscriminator();

	void setDiscriminator(@Nonnull final T discriminator);
}
