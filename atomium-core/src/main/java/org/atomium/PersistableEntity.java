package org.atomium;

public interface PersistableEntity<PK> extends Entity<PK> {

	void setId(PK pk);
	
}
