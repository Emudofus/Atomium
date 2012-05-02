package org.atomium;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.atomium.util.Function1;
import org.atomium.util.query.Query;
import org.atomium.util.query.QueryBuilderFactory;

public interface EntityManager {
	
	void start();
	void stop();

	QueryBuilderFactory builder();
	
	<T> T query(Query query, Function1<T, ResultSet> function);
	void execute(Query query);
	
}
