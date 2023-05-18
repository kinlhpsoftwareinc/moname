package com.kinlhp.moname.commons.jpa;

import java.io.Serializable;

import jakarta.annotation.Nonnull;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener that enables read-only behavior for read-only entities.
 */
public final class ReadOnlyListener<T extends AbstractReadOnly<PK>, PK extends Serializable> {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(ReadOnlyListener.class);

	@Nonnull
	private static final String MESSAGE_TEMPLATE = "{} is ready-only and cannot be {}.";

	/**
	 * Prevents new entities from being inserted.
	 *
	 * @param entity Entity to be inserted
	 * @throws ReadOnlyEntityException when trying to insert a read-only entity
	 */
	@PrePersist
	@SuppressWarnings(value = "unused")
	void onPrePersist(@Nonnull final T entity) {
		LOG.error(MESSAGE_TEMPLATE, entity.getClass().getSimpleName(), "persisted");
		throwException(entity);
	}

	/**
	 * Prevents entities from being removed.
	 *
	 * @param entity Entity to be removed
	 * @throws ReadOnlyEntityException when trying to remove a read-only entity
	 */
	@PreRemove
	@SuppressWarnings(value = "unused")
	void onPreRemove(@Nonnull final T entity) {
		LOG.error(MESSAGE_TEMPLATE, entity.getClass().getSimpleName(), "removed");
		throwException(entity);
	}

	/**
	 * Prevents entities from being updated.
	 *
	 * @param entity Entity to be updated
	 * @throws ReadOnlyEntityException when trying to update a read-only entity
	 */
	@PreUpdate
	@SuppressWarnings(value = "unused")
	void onPreUpdate(@Nonnull final T entity) {
		LOG.error(MESSAGE_TEMPLATE, entity.getClass().getSimpleName(), "updated");
		throwException(entity);
	}

	/**
	 * Throws an {@link ReadOnlyEntityException}.
	 */
	@SuppressWarnings(value = "unchecked")
	private void throwException(@Nonnull final T entity) {
		throw new ReadOnlyEntityException(entity.getClass());
	}
}
