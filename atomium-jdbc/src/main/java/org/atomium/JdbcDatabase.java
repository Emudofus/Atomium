package org.atomium;

import com.google.common.collect.ImmutableSet;
import org.atomium.converters.JodaConverter;
import org.atomium.dialects.SqlDialects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

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
        DatabaseBuilder.register("^jdbc:.+", PROVIDER);
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
        getRegistry().register(JodaConverter.PROVIDER);
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
    public <T> T findOne(Ref<T> ref) {
        SqlQuery query = dialect.read(checkNotNull(ref));

        try (ResultSet rset = query.query(connection)) {
            if (!rset.next()) {
                throw new DatabaseException.NotFound(ref);
            }
            if (!rset.isLast()) {
                throw new DatabaseException.NonUnique();
            }

            return ref.getEntityMetadata().map(NamedValues.of(rset));
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    @Override
    public <T> Set<T> all(Class<T> target) {
        Metadata<T> meta = metadataOf(target);
        SqlQuery query = dialect.read(meta);

        try (ResultSet rset = query.query(connection)) {
            if (!rset.isAfterLast()) {
                throw new DatabaseException.NotFound("can't find any data");
            }

            NamedValues values = NamedValues.of(rset);
            ImmutableSet.Builder<T> builder = ImmutableSet.builder();

            while (rset.next()) {
                T instance = meta.map(values);
                builder.add(instance);
            }

            return builder.build();
        } catch (SQLException e) {
            throw propagate(e);
        }
    }
}
