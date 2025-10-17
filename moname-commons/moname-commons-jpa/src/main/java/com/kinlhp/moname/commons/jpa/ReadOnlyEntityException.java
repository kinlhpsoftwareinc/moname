package com.kinlhp.moname.commons.jpa;

import java.io.Serial;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import lombok.Getter;

import com.kinlhp.moname.commons.jpa.entity.AbstractReadOnly;

/**
 * Exception for read-only entities.
 */
public final class ReadOnlyEntityException extends UnsupportedOperationException {

	@Serial
	private static final long serialVersionUID = -8064323672577023230L;

	private static final String MESSAGE_KEY = "jpa.ReadOnlyEntity.message";

	private static final String RESOURCE_BUNDLE_BASE_NAME = "JpaMessages";

	@Getter
	private final Class<? extends AbstractReadOnly<? extends Serializable>> type;

	@Getter
	private final String event;

	@SuppressWarnings("java:S119")
	public <T extends AbstractReadOnly<PK>, PK extends Serializable> ReadOnlyEntityException(final Class<T> type,
			final String event) {
		super(MESSAGE_KEY);
		this.type = type;
		this.event = event;
	}

	@Override
	public String getLocalizedMessage() {
		final var resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME);
		final var localizedMessage = resourceBundle.getString(MESSAGE_KEY);
		return MessageFormat.format(localizedMessage, type.getSimpleName());
	}
}
