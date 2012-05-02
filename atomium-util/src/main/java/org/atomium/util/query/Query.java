package org.atomium.util.query;

public interface Query {

	void setParameter(String field, Object obj);
	
	String toString();
	
}
