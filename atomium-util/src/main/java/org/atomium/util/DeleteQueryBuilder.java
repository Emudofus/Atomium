package org.atomium.util;

public interface DeleteQueryBuilder {

	DeleteQueryBuilder where(String field, Op op);
	DeleteQueryBuilder where(String field, Op op, Object value);
	
	DeleteQueryBuilder and(String field, Op op);
	DeleteQueryBuilder and(String field, Op op, Object value);
	
	DeleteQueryBuilder or(String field, Op op);
	DeleteQueryBuilder or(String field, Op op, Object value);
	
}
