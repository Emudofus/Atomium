package org.atomium.util.query.mysql;

import org.atomium.util.Entity;
import org.atomium.util.query.Op;
import org.atomium.util.query.Order;

public class MySqlOp {

	public static String print(Op op) {
		switch (op) {
		case DIF:
			return " != ";
		case EQ:
			return " = ";
		case INF:
			return " < ";
		case INF_EQ:
			return " <= ";
		case LIKE:
			return " LIKE ";
		case SUP:
			return " > ";
		case SUP_EQ:
			return " >= ";
		}
		return null;
	}
	
	public static String print(Order order) {
		switch (order){
		case ASC:
			return "ASC";
		case DESC:
			return "DESC";
		}
		return null;
	}
	
	public static String toString(Object obj) {
		if (obj instanceof Boolean) {
			return ((Boolean)obj) ? "1" : "0";
		}
		else if (obj instanceof Enum<?>) {
			return Integer.toString(((Enum<?>)obj).ordinal());
		}
		else if (obj instanceof Entity<?>) {
			return ((Entity<?>)obj).id().toString();
		}
		return obj.toString();
	}
	
}
