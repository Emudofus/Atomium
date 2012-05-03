package org.atomium.repository;

import org.atomium.util.Entity;

public interface DeletableEntityRepository<PK, T extends Entity<PK>>
	extends EntityRepository<PK, T>
{
	/**
	 * delete entity
	 * @param entity
	 */
	void delete(T entity);
}
