package org.atomium.repository;

import java.util.List;

import org.atomium.LazyReference;
import org.atomium.util.Entity;
import org.atomium.util.Filter;

public interface BaseEntityRepository<PK, T extends Entity<PK>>
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
	List<T> filter(Filter<T> filter);
	
	/**
	 * return a lazy reference
	 * @param pk entity's id
	 * @return reference
	 */
	LazyReference<PK, T> getLazyReference(PK pk);
	
}
