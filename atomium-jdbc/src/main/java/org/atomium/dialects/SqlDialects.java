package org.atomium.dialects;

import org.atomium.JdbcDatabaseMetadata;
import org.atomium.SqlDialectInterface;
import org.atomium.dialects.DefaultSqlDialect;
import org.atomium.dialects.HsqldbDialect;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static com.google.common.base.Throwables.propagate;

/**
 * @author Blackrush
 */
public final class SqlDialects {
    private SqlDialects() {}

    public static SqlDialectInterface forDatabaseMetaData(DatabaseMetaData meta) {
        try {
            JdbcDatabaseMetadata meta0 = JdbcDatabaseMetadata.fromJDBC(meta);

            String productName = meta.getDatabaseProductName();
            if (productName.equalsIgnoreCase("hsqldb")) {
                return new HsqldbDialect(meta0);
            } else {
                return new DefaultSqlDialect(meta0);
            }
        } catch (SQLException e) {
            throw propagate(e);
        }
    }
}
