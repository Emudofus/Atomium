package org.atomium.repository;

import org.atomium.Entity;

public interface PersistableEntityRepository<PK, T extends Entity<PK>>
	extends EntityRepository<PK, T>
{
	/**
	 * create entity in the database
	 * @param entity
	 */
	void persist(T entity);
}
