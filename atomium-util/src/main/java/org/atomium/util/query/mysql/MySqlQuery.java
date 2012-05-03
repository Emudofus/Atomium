package org.atomium.util.query.mysql;

import org.atomium.util.query.Query;

public class MySqlQuery implements Query {
	
	private String query;
	
	public MySqlQuery(String query) {
		this.query = query;
	}

	public void setParameter(String field, Object obj) {
		query = query.replace("#" + field + "#", MySqlOp.toString(obj));
	}
	
	@Override
	public String toString() {
		return query;
	}

}
