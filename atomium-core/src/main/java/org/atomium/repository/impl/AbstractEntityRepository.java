package org.atomium.repository.impl;

import org.atomium.Entity;
import org.atomium.EntityManager;
import org.atomium.repository.DeletableEntityRepository;
import org.atomium.repository.PersistableEntityRepository;
import org.atomium.util.query.Query;

public abstract class AbstractEntityRepository<PK, T extends Entity<PK>>
	extends AbstractSaveableEntityRepository<PK, T>
	implements PersistableEntityRepository<PK, T>, DeletableEntityRepository<PK, T>
{

	protected AbstractEntityRepository(EntityManager em) {
		super(em);
	}
	
	protected abstract Query buildDeleteQuery(T entity);
	protected abstract Query buildPersistQuery(T entity);

	public void delete(T entity) {
		Query query = buildDeleteQuery(entity);
		em.execute(query);
	}

	public void persist(T entity) {
		Query query = buildPersistQuery(entity);
		em.execute(query);
	}

}
