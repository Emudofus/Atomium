package org.atomium;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import org.atomium.converters.JodaConverter;
import org.atomium.dialects.SqlDialects;
import org.atomium.metadata.MetadataRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;

/**
 * @author Blackrush
 */
public final class JdbcDatabase extends Database {
    public static final DatabaseProvider PROVIDER = new DatabaseProvider() {
        public DatabaseInterface get(final String url, final String user, final String password, MetadataRegistry registry) {
            return of(url, user, password, registry);
        }
    };

    static {
        Database.register("^jdbc:.+", PROVIDER);
    }

    private final Supplier<Connection> connection;
    private final SqlDialectInterface dialect;
    private final List<JdbcSession> sessions = Lists.newArrayList();

    private JdbcDatabase(Supplier<Connection> connection, SqlDialectInterface dialect, MetadataRegistry registry) {
        super(registry);
        this.connection = checkNotNull(connection);
        this.dialect = checkNotNull(dialect);
    }

    public static JdbcDatabase of(Supplier<Connection> connection, SqlDialectInterface dialect, MetadataRegistry registry) {
        return new JdbcDatabase(connection, dialect, registry);
    }

    public static JdbcDatabase of(final String url, final String user, final String password, MetadataRegistry registry) {
        Supplier<Connection> supplier = new Supplier<Connection>() {
            public Connection get() {
                try {
                    return DriverManager.getConnection(url, user, password);
                } catch (SQLException e) {
                    throw propagate(e);
                }
            }
        };

        try (Connection con = supplier.get()) {
            return of(supplier, SqlDialects.forDatabaseMetaData(con.getMetaData()), registry);
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    @Override
    public void load() {
        getRegistry().register(new JodaConverter());
    }

    @Override
    public void close() {
        for (int i = 0; i < sessions.size(); i++) {
            sessions.remove(0).close();
        }
    }

    @Override
    public SqlDialectInterface getDialect() {
        return dialect;
    }

    @Override
    public JdbcSession createSession() {
        JdbcSession session = new JdbcSession(this, connection.get());
        sessions.add(session);
        return session;
    }

    void onClosed(JdbcSession session) {
        sessions.remove(session);
    }

    Supplier<Connection> getConnection() {
        return connection;
    }
}
