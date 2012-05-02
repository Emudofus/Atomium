package org.atomium.util.query;

public interface InsertQueryBuilder {

	InsertQueryBuilder value(String field);
	InsertQueryBuilder value(String field, Object value);
	
}
