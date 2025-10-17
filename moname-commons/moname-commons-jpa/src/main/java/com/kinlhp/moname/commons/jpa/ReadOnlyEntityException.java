package com.kinlhp.moname.commons.jpa;

import java.io.Serial;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import jakarta.annotation.Nonnull;

import lombok.Getter;

import com.kinlhp.moname.commons.jpa.entity.AbstractReadOnly;

/**
 * Exception for read-only entities.
 */
public final class ReadOnlyEntityException extends UnsupportedOperationException {

	@Serial
	private static final long serialVersionUID = -8064323672577023230L;

	@Nonnull
	private static final String MESSAGE_KEY = "jpa.ReadOnlyEntity.message";

	@Nonnull
	private static final String RESOURCE_BUNDLE_BASE_NAME = "JpaMessages";

	@Getter
	@Nonnull
	private final Class<? extends AbstractReadOnly<? extends Serializable>> type;

	@Getter
	@Nonnull
	private final String event;

	@SuppressWarnings("java:S119")
	public <T extends AbstractReadOnly<PK>, PK extends Serializable> ReadOnlyEntityException(
			@Nonnull final Class<T> type, @Nonnull final String event) {
		super(MESSAGE_KEY);
		this.type = type;
		this.event = event;
	}

	@Nonnull
	@Override
	public String getLocalizedMessage() {
		@Nonnull final var resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME);
		@Nonnull final var localizedMessage = resourceBundle.getString(MESSAGE_KEY);
		return MessageFormat.format(localizedMessage, type.getSimpleName());
	}
}
