package org.atomium;

import org.atomium.repository.EntityRepository;
import org.atomium.util.Reference;

public class LazyReference<PK, T extends Entity<PK>> implements Reference<T> {
	
	private PK pk;
	private EntityRepository<PK, T> repo;
	
	private T entity;
	
	public LazyReference(PK pk, EntityRepository<PK, T> repo) {
		this.pk = pk;
		this.repo = repo;
	}

	public PK getPk() {
		return pk;
	}

	public void setPk(PK pk) {
		this.pk = pk;
	}

	@Override
	public T get() {
		if (entity == null) {
			entity = repo.find(pk);
		}
		return entity;
	}

	@Override
	public void set(T o) {
		this.entity = o;
	}

	@Override
	public boolean isNull() {
		return entity == null;
	}

}
