package org.atomium;

import com.google.common.collect.Maps;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static com.google.common.base.Throwables.propagate;

/**
 * @author Blackrush
 */
public class JdbcDatabaseMetadata {
    public static final String TYPE_NAME_COLUMN = "TYPE_NAME",
                               DATA_TYPE_COLUMN = "DATA_TYPE",
                               CREATE_PARAMS_COLUMN = "CREATE_PARAMS",
                               LENGTH_PARAM = "LENGTH";

    public class SqlType {
        public final int id;
        public final String name;
        public final String createParams;

        public SqlType(int id, String name, String createParams) {
            this.id = id;
            this.name = name;
            this.createParams = createParams;
        }

        public boolean hasParam(String param) {
            return param.equalsIgnoreCase(createParams);
        }
    }

    private final Map<Integer, SqlType> nativeTypes = Maps.newHashMap();

    public SqlType getType(int sqlTypeId) {
        return nativeTypes.get(sqlTypeId);
    }

    public SqlType getType(Class<?> clazz) {
        return getType(SQL.javaClassToSqlType(clazz));
    }

    private void add(ResultSet rset) throws SQLException {
        SqlType type = new SqlType(
                rset.getInt(DATA_TYPE_COLUMN),
                rset.getString(TYPE_NAME_COLUMN),
                rset.getString(CREATE_PARAMS_COLUMN)
        );

        if (!nativeTypes.containsKey(type.id)) {
            nativeTypes.put(type.id, type);
        }
    }

    public static JdbcDatabaseMetadata fromJDBC(DatabaseMetaData meta) {
        try (ResultSet rset = meta.getTypeInfo()) {
            JdbcDatabaseMetadata result = new JdbcDatabaseMetadata();
            while (rset.next()) {
                result.add(rset);
            }
            return result;
        } catch (SQLException e) {
            throw propagate(e);
        }
    }
}
