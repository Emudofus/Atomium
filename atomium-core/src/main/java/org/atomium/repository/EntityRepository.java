package org.atomium.repository;

import java.util.Collection;

import org.atomium.Entity;
import org.atomium.util.Filter;

public interface EntityRepository<PK, T extends Entity<PK>>
	extends Repository, Iterable<T>
{

	/**
	 * find an entity by its primary key (or id)
	 * @param pk primary key (or id)
	 * @return entity
	 */
	T find(PK pk);
	
	/**
	 * number of entities loaded
	 * @return int
	 */
	int count();
	
	/**
	 * filter entities
	 * @param filter the filter
	 * @return filtered entities
	 */
	Collection<T> filter(Filter<T> filter);
	
}
