package org.atomium.util.query;

public interface QueryBuilderFactory {

	SelectQueryBuilder select(String table, String... fields);
	
	InsertQueryBuilder insert(String table);
	
	UpdateQueryBuilder update(String table);
	
	DeleteQueryBuilder delete(String table);
	
}
