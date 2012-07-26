package org.atomium.repository.impl;

import org.atomium.EntityManager;
import org.atomium.LazyReference;
import org.atomium.PersistableEntity;
import org.atomium.exception.LoadingException;
import org.atomium.repository.EntityRepository;
import org.atomium.util.pk.PrimaryKeyGenerator;
import org.atomium.util.query.Query;

public abstract class AbstractEntityRepository<PK, T extends PersistableEntity<PK>>
	extends AbstractSaveableEntityRepository<PK, T>
	implements EntityRepository<PK, T>
{
	
	private final PrimaryKeyGenerator<PK> pkgen;

	protected AbstractEntityRepository(EntityManager em, PrimaryKeyGenerator<PK> pkgen) {
		super(em);
		this.pkgen = pkgen;
	}
	
	protected abstract Query buildDeleteQuery(T entity);
	protected abstract Query buildPersistQuery(T entity);

	protected void afterLoading() throws LoadingException {
		for (PK pk : entities.keySet()) {
			pkgen.setMax(pk);
		}
	}

	public void delete(T entity) {
		Query query = buildDeleteQuery(entity);
		em.execute(query);
		
		entities.remove(entity.id());
	}

	public void deleteLater(T entity) {
		Query query = buildDeleteQuery(entity);
		em.executeLater(query);
		
		entities.remove(entity.id());
	}

	public void persist(T entity) {
		entity.setId(pkgen.next());
		Query query = buildPersistQuery(entity);
		em.execute(query);
		
		entities.put(entity.id(), entity);
	}

	public void persistLater(T entity) {
		entity.setId(pkgen.next());
		Query query = buildPersistQuery(entity);
		em.executeLater(query);
		
		entities.put(entity.id(), entity);
	}
	
	public LazyReference<PK,T> getLazyReference(PK pk) {
		return new LazyReference<PK, T>(pk, this);
	}

}
