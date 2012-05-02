package org.atomium.util.query.mysql;

import org.atomium.util.query.DeleteQueryBuilder;
import org.atomium.util.query.InsertQueryBuilder;
import org.atomium.util.query.QueryBuilderFactory;
import org.atomium.util.query.SelectQueryBuilder;
import org.atomium.util.query.UpdateQueryBuilder;

public class MySqlQueryBuilder implements QueryBuilderFactory {

	public SelectQueryBuilder select(String table, String... fields) {
		return new MySqlSelectQueryBuilder(table, fields);
	}

	public InsertQueryBuilder insert(String table) {
		return new MySqlInsertQueryBuilder(table);
	}

	public UpdateQueryBuilder update(String table) {
		return new MySqlUpdateQueryBuilder(table);
	}

	public DeleteQueryBuilder delete(String table) {
		return new MySqlDeleteQueryBuilder(table);
	}

}
