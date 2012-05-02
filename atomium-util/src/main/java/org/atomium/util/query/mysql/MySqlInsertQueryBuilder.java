package org.atomium.util.query.mysql;

import java.util.HashMap;
import java.util.Map;

import org.atomium.util.query.InsertQueryBuilder;
import org.atomium.util.query.Query;

public class MySqlInsertQueryBuilder implements InsertQueryBuilder {
	
	private String table;
	
	private Map<String, Object> values = new HashMap<String, Object>();

	public MySqlInsertQueryBuilder(String table) {
		this.table = table;
	}

	public InsertQueryBuilder value(String field) {
		values.put(field, null);
		
		return this;
	}

	public InsertQueryBuilder value(String field, Object value) {
		values.put(field, value);
		
		return this;
	}
	
	public Query toQuery() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("INSERT INTO `").append(table).append("`");
		
		sb.append('(');
		boolean first = true;
		for (String column : values.keySet()) {
			if (first) first = false;
			else sb.append(", ");
			sb.append('`').append(column).append("`");
		}
		
		sb.append(") VALUES(");
		first = true;
		for (Object value : values.values()) {
			if (first) first = false;
			else sb.append(", ");
			sb.append('\'').append(value.toString()).append('\'');
		}
		sb.append(");");

		return new MySqlQuery(sb.toString());
	}

}