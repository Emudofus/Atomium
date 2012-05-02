package org.atomium.repository.impl;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;

import org.atomium.Entity;
import org.atomium.EntityManager;
import org.atomium.exception.LoadingException;
import org.atomium.repository.SaveableEntityRepository;
import org.atomium.util.Filter;
import org.atomium.util.Function1;
import org.atomium.util.query.Query;

public abstract class AbstractLiveEntityRepository<PK, T extends Entity<PK>>
	implements SaveableEntityRepository<PK, T>
{
	
	protected final EntityManager em;
	
	public AbstractLiveEntityRepository(EntityManager em) {
		this.em = em;
	}
	
	protected abstract Query buildLoadQuery(PK pk);
	protected abstract Query buildSaveQuery(T entity);
	protected abstract T load(ResultSet result);
	
	protected T find(Query query) {
		return em.query(query, new Function1<T, ResultSet>() {
			public T invoke(ResultSet arg1) throws Exception {
				return load(arg1);
			}
		});
	}

	@Override
	public T find(PK pk) {
		return find(buildLoadQuery(pk));
	}

	@Override
	public int count() {
		return 0;
	}

	@Override
	public Collection<T> filter(Filter<T> filter) {
		return null;
	}

	@Override
	public int load() throws LoadingException {
		return 0;
	}

	@Override
	public boolean loaded() {
		return true;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			public boolean hasNext() {
				return false;
			}

			public T next() {
				return null;
			}

			public void remove() {
			}
		};
	}

	@Override
	public void save() {
	}

	@Override
	public void save(T entity) {
		Query query = buildSaveQuery(entity);
		em.execute(query);
	}
	
}
