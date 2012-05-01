package org.atomium.util.mysql;

import org.atomium.util.Op;
import org.atomium.util.Order;

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
	
}
