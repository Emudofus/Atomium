package org.atomium;

import java.sql.ResultSet;

import org.atomium.util.Function1;
import org.atomium.util.query.QueryBuilder;
import org.atomium.util.query.SelectQueryBuilder;

public interface EntityManager {

	QueryBuilder builder();
	
	<T> T query(SelectQueryBuilder query, Function1<T, ResultSet> function);
	
}
