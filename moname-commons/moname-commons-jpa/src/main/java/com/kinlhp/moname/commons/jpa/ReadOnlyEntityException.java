package com.kinlhp.moname.commons.jpa;

import java.io.Serial;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Exception for read-only entities.
 */
public final class ReadOnlyEntityException extends UnsupportedOperationException {

	@Serial
	private static final long serialVersionUID = 6261917093295309842L;

	private static final String MESSAGE_KEY = "jpa.ReadOnlyEntity.message";
	private static final String RESOURCE_BUNDLE_BASE_NAME = "JpaMessages";

	private final Class<? extends AbstractReadOnly<? extends Serializable>> clazz;

	<T extends AbstractReadOnly<PK>, PK extends Serializable> ReadOnlyEntityException(final Class<T> clazz) {
		super(MESSAGE_KEY);
		this.clazz = clazz;
	}

	@Override
	public String getLocalizedMessage() {
		final var resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME);
		final var localizedMessage = resourceBundle.getString(MESSAGE_KEY);
		return MessageFormat.format(localizedMessage, clazz.getSimpleName());
	}

}
