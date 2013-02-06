package org.atomium.persistence;

import com.google.common.collect.Lists;
import org.atomium.Entity;
import org.atomium.PersistenceStrategy;
import org.atomium.Query;
import org.atomium.entity.EntityMetadata;
import org.atomium.entity.EntityProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;
import static java.util.Collections.singleton;

/**
 * you are only responsible of {@link Connection}'s creation (this class automatically close the given connection)
 * @author blackrush
 */
public class JdbcPersistenceStrategy implements PersistenceStrategy {
    private final Dialect dialect;
    private final Connection connection;

    public JdbcPersistenceStrategy(Dialect dialect, Connection connection) {
        this.dialect = checkNotNull(dialect);
        this.connection = checkNotNull(connection);
    }

    @Override
    public void setUp() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    @Override
    public void tearDown() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    protected void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    protected void setParameters(Query query, PreparedStatement statement) throws SQLException{
        int i = 1;
        for (Object parameter : query.getParameters()) {
            statement.setObject(i++, parameter); // TODO export
        }
    }

    /**
     * you must commit by yourself
     * @param query query to execute
     */
    protected void execute(Query query) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query.getCommand());
            setParameters(query, statement);

            statement.executeUpdate();
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

    protected ResultSet query(Query query) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query.getCommand());
            setParameters(query, statement);

            return statement.executeQuery();
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
                    property.set(entity, value); // TODO extract
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
    public <T extends Entity> void create(Iterable<T> entities, EntityMetadata<T> metadata) {
        for (T entity : entities) {
            execute(dialect.insert(metadata, entity));
        }
        commit();
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
    public <T extends Entity> void update(Iterable<T> entities, EntityMetadata<T> metadata) {
        for (T entity : entities) {
            execute(dialect.update(metadata, entity));
        }
        commit();
    }

    @Override
    public <T extends Entity> void destroy(T entity, EntityMetadata<T> metadata) {
        destroy(singleton(entity), metadata);
    }

    @Override
    public <T extends Entity> void destroy(Iterable<T> entities, EntityMetadata<T> metadata) {
        for (T entity : entities) {
            execute(dialect.delete(metadata, entity));
        }
        commit();
    }
}
