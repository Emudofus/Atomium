package org.atomium;

import org.atomium.converters.JodaConverter;
import org.atomium.dialects.SqlDialects;
import org.atomium.metadata.MetadataRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;

/**
 * @author Blackrush
 */
public final class JdbcDatabase extends Database {
    public static final DatabaseProvider PROVIDER = new DatabaseProvider() {
        public DatabaseInterface get(String url, String user, String password, MetadataRegistry registry) {
            try {
                Connection connection = DriverManager.getConnection(url, user, password);
                SqlDialectInterface dialect = SqlDialects.forDatabaseMetaData(connection.getMetaData());

                return of(connection, dialect, registry);
            } catch (SQLException e) {
                throw propagate(e);
            }
        }
    };

    static {
        Database.register("^jdbc:.+", PROVIDER);
    }

    private final Connection connection;
    private final SqlDialectInterface dialect;

    private JdbcDatabase(Connection connection, SqlDialectInterface dialect, MetadataRegistry registry) {
        super(registry);
        this.connection = checkNotNull(connection);
        this.dialect = checkNotNull(dialect);
    }

    public static JdbcDatabase of(Connection connection, SqlDialectInterface dialect, MetadataRegistry registry) {
        return new JdbcDatabase(connection, dialect, registry);
    }

    @Override
    public void load() {
        getRegistry().register(new JodaConverter());
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    @Override
    public SqlDialectInterface getDialect() {
        return dialect;
    }

    @Override
    public SessionInterface createSession() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
