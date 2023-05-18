package com.kinlhp.moname.commons.jpa;

import java.io.Serial;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import jakarta.annotation.Nonnull;

/**
 * Exception for read-only entities.
 */
public final class ReadOnlyEntityException extends UnsupportedOperationException {

	@Serial
	private static final long serialVersionUID = -7868678367803826835L;

	@Nonnull
	private static final String MESSAGE_KEY = "jpa.ReadOnlyEntity.message";
	@Nonnull
	private static final String RESOURCE_BUNDLE_BASE_NAME = "JpaMessages";

	@Nonnull
	private final Class<? extends AbstractReadOnly<? extends Serializable>> aClass;

	<T extends AbstractReadOnly<PK>, PK extends Serializable> ReadOnlyEntityException(@Nonnull final Class<T> aClass) {
		super(MESSAGE_KEY);
		this.aClass = aClass;
	}

	@Nonnull
	@Override
	public String getLocalizedMessage() {
		@Nonnull final var resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME);
		@Nonnull final var localizedMessage = resourceBundle.getString(MESSAGE_KEY);
		return MessageFormat.format(localizedMessage, aClass.getSimpleName());
	}
}
