package org.atomium;

import java.sql.Types;

/**
 * @author Blackrush
 */
public final class SQL {
    /**
     * http://docs.oracle.com/javase/6/docs/technotes/guides/jdbc/getstart/mapping.html#1034737
     * @param clazz java class
     * @return sql type
     */
    public static int javaClassToSqlType(Class<?> clazz) {
        if (clazz == String.class) {
            return Types.VARCHAR;
        } else if (clazz == java.math.BigDecimal.class) {
            return Types.NUMERIC;
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return Types.BIT;
        } else if (clazz == Integer.class || clazz == int.class) {
            return Types.INTEGER;
        } else if (clazz == Long.class || clazz == long.class) {
            return Types.BIGINT;
        } else if (clazz == Float.class || clazz == float.class) {
            return Types.REAL;
        } else if (clazz == Double.class || clazz == double.class) {
            return Types.DOUBLE;
        } else if (clazz.isArray() && clazz.getComponentType() == Byte.class || clazz.getComponentType() == byte.class) {
            return Types.VARBINARY;
        } else if (clazz == java.sql.Date.class) {
            return Types.DATE;
        } else if (clazz == java.sql.Time.class) {
            return Types.TIME;
        } else if (clazz == java.sql.Timestamp.class) {
            return Types.TIMESTAMP;
        } else if (java.sql.Clob.class.isAssignableFrom(clazz)) {
            return Types.CLOB;
        } else if (java.sql.Blob.class.isAssignableFrom(clazz)) {
            return Types.BLOB;
        } else if (java.sql.Array.class.isAssignableFrom(clazz)) {
            return Types.ARRAY;
        } else if (java.sql.Struct.class.isAssignableFrom(clazz)) {
            return Types.STRUCT;
        } else if (java.sql.Ref.class.isAssignableFrom(clazz)) {
            return Types.REF;
        } else {
            return Types.JAVA_OBJECT;
        }
    }

    /**
     * http://docs.oracle.com/javase/6/docs/technotes/guides/jdbc/getstart/mapping.html#1051555
     * @param type sql type
     * @return java class
     * @throws IllegalArgumentException if this function can not determine java class
     */
    public static Class<?> sqlTypeToJavaClass(int type) {
        switch (type) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGNVARCHAR:
                return String.class;
            case Types.NUMERIC:
            case Types.DECIMAL:
                return java.math.BigDecimal.class;
            case Types.BIT:
                return Boolean.class;
            case Types.TINYINT:
                return Byte.class;
            case Types.SMALLINT:
                return Short.class;
            case Types.INTEGER:
                return Integer.class;
            case Types.BIGINT:
                return Long.class;
            case Types.REAL:
                return Float.class;
            case Types.FLOAT:
            case Types.DOUBLE:
                return Double.class;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                return byte[].class;
            case Types.DATE:
                return java.sql.Date.class;
            case Types.TIME:
                return java.sql.Time.class;
            case Types.TIMESTAMP:
                return java.sql.Timestamp.class;
            case Types.CLOB:
                return java.sql.Clob.class;
            case Types.BLOB:
                return java.sql.Blob.class;
            case Types.ARRAY:
                return java.sql.Array.class;
            case Types.STRUCT:
                return java.sql.Struct.class;
            case Types.REF:
                return java.sql.Ref.class;
            default:
                throw new IllegalArgumentException("can't determines java class of " + type + " type");
        }
    }
}
