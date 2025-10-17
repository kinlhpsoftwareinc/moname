package com.kinlhp.moname.commons.jpa.listener;

import java.io.Serializable;

import jakarta.annotation.Nonnull;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kinlhp.moname.commons.jpa.ReadOnlyEntityException;
import com.kinlhp.moname.commons.jpa.entity.AbstractReadOnly;

/**
 * Listener that enables read-only behavior for read-only entities.
 */
@SuppressWarnings("java:S119")
public final class ReadOnlyListener<T extends AbstractReadOnly<PK>, PK extends Serializable> {

	@Nonnull
	private static final Logger LOG = LoggerFactory.getLogger(ReadOnlyListener.class);

	@Nonnull
	private static final String MESSAGE_TEMPLATE = "{} is read-only and cannot be {}";

	/**
	 * Prevents new entities from being inserted.
	 *
	 * @param entity Entity to be inserted
	 * @throws ReadOnlyEntityException when trying to insert a read-only entity
	 */
	@PrePersist
	void onPrePersist(@Nonnull final T entity) {
		LOG.error(MESSAGE_TEMPLATE, entity.getClass().getSimpleName(), "persisted");
		throwException(entity, "persist");
	}

	/**
	 * Prevents entities from being removed.
	 *
	 * @param entity Entity to be removed
	 * @throws ReadOnlyEntityException when trying to remove a read-only entity
	 */
	@PreRemove
	void onPreRemove(@Nonnull final T entity) {
		LOG.error(MESSAGE_TEMPLATE, entity.getClass().getSimpleName(), "removed");
		throwException(entity, "remove");
	}

	/**
	 * Prevents entities from being updated.
	 *
	 * @param entity Entity to be updated
	 * @throws ReadOnlyEntityException when trying to update a read-only entity
	 */
	@PreUpdate
	void onPreUpdate(@Nonnull final T entity) {
		LOG.error(MESSAGE_TEMPLATE, entity.getClass().getSimpleName(), "updated");
		throwException(entity, "update");
	}

	/**
	 * Throws an {@link ReadOnlyEntityException ReadOnlyEntityException}.
	 */
	private void throwException(@Nonnull final T entity, @Nonnull final String event) {
		//noinspection unchecked
		throw new ReadOnlyEntityException(entity.getClass(), event);
	}
}
