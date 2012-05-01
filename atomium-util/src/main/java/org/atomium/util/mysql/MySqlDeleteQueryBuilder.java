package org.atomium.util.mysql;

import org.atomium.util.DeleteQueryBuilder;
import org.atomium.util.Op;

public class MySqlDeleteQueryBuilder implements DeleteQueryBuilder {
	
	private String table;
	
	private StringBuilder sb = new StringBuilder();

	public MySqlDeleteQueryBuilder(String table) {
		this.table = table;
	}
	
	private void print(Op op) {
		sb.append(MySqlOp.print(op));
	}

	public DeleteQueryBuilder where(String field, Op op) {
		sb.append("WHERE `").append(field).append("`");
		print(op);
		sb.append("'#").append(field).append("#'");
		
		return this;
	}

	public DeleteQueryBuilder where(String field, Op op, Object value) {
		sb.append("WHERE `").append(field).append("`");
		print(op);
		sb.append("'").append(value.toString()).append("'");
		
		return this;
	}

	public DeleteQueryBuilder and(String field, Op op) {
		sb.append(" AND `").append(field).append("`");
		print(op);
		sb.append("'#").append(field).append("#'");
		
		return this;
	}

	public DeleteQueryBuilder and(String field, Op op, Object value) {
		sb.append(" AND `").append(field).append("`");
		print(op);
		sb.append("'").append(value.toString()).append("'");
		
		return this;
	}

	public DeleteQueryBuilder or(String field, Op op) {
		sb.append(" OR `").append(field).append("`");
		print(op);
		sb.append("'#").append(field).append("#'");
		
		return this;
	}

	public DeleteQueryBuilder or(String field, Op op, Object value) {
		sb.append(" OR `").append(field).append("`");
		print(op);
		sb.append("'").append(value.toString()).append("'");
		
		return this;
	}
	
	@Override
	public String toString() {
		return "DELETE FROM `" + table + "` " + sb.toString() + ";";
	}

}
