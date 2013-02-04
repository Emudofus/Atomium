package org.atomium.persistence;

import com.google.common.collect.Lists;
import org.atomium.Entity;
import org.atomium.PersistenceStrategy;
import org.atomium.entity.EntityMetadata;
import org.atomium.entity.EntityProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;
import static java.util.Collections.singleton;

/**
 * @author blackrush
 */
public class JdbcPersistenceStrategy implements PersistenceStrategy {
    private final Dialect dialect;
    private final Connection connection;

    public JdbcPersistenceStrategy(Dialect dialect, Connection connection) {
        this.dialect = checkNotNull(dialect);
        this.connection = checkNotNull(connection);
    }

    protected void execute(String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();

            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw propagate(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignored) { }
            }
        }
    }

    protected ResultSet query(String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();

            return statement.executeQuery(query);
        } catch (SQLException e) {
            throw propagate(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignored) { }
            }
        }
    }

    protected <T extends Entity> Iterable<T> map(ResultSet rset, EntityMetadata<T> metadata) {
        try {
            rset.beforeFirst();

            List<EntityProperty<T>> properties = metadata.getProperties();
            List<T> result = Lists.newArrayList();

            while (rset.next()) {
                T entity = metadata.create();

                for (EntityProperty<T> property : properties) {
                    Object value = rset.getObject(property.getName());
                    property.set(entity, value);
                }

                result.add(entity);
            }

            return result;
        } catch (SQLException e) {
            throw propagate(e);
        } finally {
            try {
                rset.close();
            } catch (SQLException ignored) { }
        }
    }

    @Override
    public <T extends Entity> void create(T entity, EntityMetadata<T> metadata) {
        create(singleton(entity), metadata);
    }

    @Override
    public <T extends Entity> void create(Iterable<T> entity, EntityMetadata<T> metadata) {
        execute(dialect.insert(entity, metadata));
    }

    @Override
    public <T extends Entity> Iterable<T> read(EntityMetadata<T> metadata) {
        return map(query(dialect.select(metadata)), metadata);
    }

    @Override
    public <T extends Entity> void update(T entity, EntityMetadata<T> metadata) {
        update(singleton(entity), metadata);
    }

    @Override
    public <T extends Entity> void update(Iterable<T> entity, EntityMetadata<T> metadata) {
        execute(dialect.update(entity, metadata));
    }

    @Override
    public <T extends Entity> void destroy(T entity, EntityMetadata<T> metadata) {
        destroy(singleton(entity), metadata);
    }

    @Override
    public <T extends Entity> void destroy(Iterable<T> entity, EntityMetadata<T> metadata) {
        execute(dialect.delete(entity, metadata));
    }
}
