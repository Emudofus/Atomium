package org.atomium.repository;

import org.atomium.util.Entity;

public interface SaveableEntityRepository<PK, T extends Entity<PK>>
	extends EntityRepository<PK, T>
{
	/**
	 * save all entities
	 */
	void save();
	
	/**
	 * save only one entity
	 * @param entity
	 */
	void save(T entity);
}
