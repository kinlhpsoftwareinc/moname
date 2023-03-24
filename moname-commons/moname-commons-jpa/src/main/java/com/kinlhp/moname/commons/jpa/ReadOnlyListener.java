package com.kinlhp.moname.commons.jpa;

import java.io.Serializable;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

/**
 * Listener that enables read-only behavior for read-only entities.
 */
public final class ReadOnlyListener<T extends AbstractReadOnly<PK>, PK extends Serializable> {

	/**
	 * Prevents new entities from being inserted.
	 *
	 * @param entity Entity to be inserted
	 * @throws ReadOnlyEntityException when trying to insert a read-only entity
	 */
	@PrePersist
	void onPrePersist(final T entity) {
		this.throwException(entity);
	}

	/**
	 * Prevents entities from being removed.
	 *
	 * @param entity Entity to be removed
	 * @throws ReadOnlyEntityException when trying to remove a read-only entity
	 */
	@PreRemove
	void onPreRemove(final T entity) {
		this.throwException(entity);
	}

	/**
	 * Prevents entities from being updated.
	 *
	 * @param entity Entity to be updated
	 * @throws ReadOnlyEntityException when trying to update a read-only entity
	 */
	@PreUpdate
	void onPreUpdate(final T entity) {
		this.throwException(entity);
	}

	/**
	 * Throws an {@link ReadOnlyEntityException}.
	 */
	@SuppressWarnings(value = {"unchecked"})
	private void throwException(final T entity) {
		throw new ReadOnlyEntityException(entity.getClass());
	}

}
