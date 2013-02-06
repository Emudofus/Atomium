package org.atomium.persistence;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.atomium.*;
import org.atomium.entity.EntityMetadata;
import org.atomium.entity.EntityProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;
import static java.util.Collections.singleton;

/**
 * you are only responsible of {@link Connection}'s creation (this class automatically close the given connection)
 * @author blackrush
 */
public class JdbcPersistenceStrategy implements PersistenceStrategy {
    private final Map<Class<?>, TypeConverter> typeConverters = Maps.newHashMap();

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

    @Override
    public void setImplicitTypeConverter(TypeConverter typeConverter) {
        typeConverters.put(typeConverter.getTargetClass(), typeConverter);
    }

    protected TypeConverter getTypeConverterOrRegister(Class<? extends TypeConverter> typeConverterClass) {
        for (TypeConverter typeConverter : typeConverters.values()) {
            if (typeConverterClass.isAssignableFrom(typeConverter.getClass())) {
                return typeConverter;
            }
        }

        try {
            TypeConverter typeConverter = typeConverterClass.newInstance();
            typeConverters.put(typeConverter.getTargetClass(), typeConverter);
            return typeConverter;
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    private <T extends Entity> TypeConverter getTypeConverter(EntityProperty<T> property) {
        return property.getTypeConverter().isPresent()
                ? getTypeConverterOrRegister(property.getTypeConverter().get())
                : typeConverters.get(property.getPropertyClass());
    }

    protected void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    protected <T extends Entity> NamedValues export(EntityMetadata<T> metadata, T entity, DatabaseContext ctx) {
        NamedValues values = NamedValues.simple();

        for (EntityProperty<T> property : metadata.getProperties()) {
            TypeConverter typeConverter = getTypeConverter(property);

            if (typeConverter != null) {
                typeConverter.export(ctx, entity, property, values);
            } else {
                values.set(property.getName(), property.get(entity));
            }
        }

        return values;
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

    protected <T extends Entity> Iterable<T> map(DatabaseContext ctx, ResultSet rset, EntityMetadata<T> metadata) {
        try {
            rset.beforeFirst();

            NamedValues values = JdbcNamedValues.of(rset);
            List<T> result = Lists.newArrayList();

            while (rset.next()) {
                T entity = createEntity(ctx, metadata, values);
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

    private <T extends Entity> T createEntity(DatabaseContext ctx, EntityMetadata<T> metadata, NamedValues values) throws SQLException {
        T entity = metadata.create();

        for (EntityProperty<T> property : metadata.getProperties()) {
            TypeConverter typeConverter = getTypeConverter(property);

            Object value = typeConverter != null
                    ? typeConverter.extract(ctx, entity, property, values)
                    : values.get(property.getName());

            property.set(entity, value);
        }

        return entity;
    }

    @Override
    public <T extends Entity> void create(DatabaseContext ctx, EntityMetadata<T> metadata, T entity) {
        create(ctx, metadata, singleton(entity));
    }

    @Override
    public <T extends Entity> void create(DatabaseContext ctx, EntityMetadata<T> metadata, Iterable<T> entities) {
        for (T entity : entities) {
            execute(dialect.insert(metadata, entity));
        }
        commit();
    }

    @Override
    public <T extends Entity> Iterable<T> read(DatabaseContext ctx, EntityMetadata<T> metadata) {
        return map(ctx, query(dialect.select(metadata)), metadata);
    }

    @Override
    public <T extends Entity> void update(DatabaseContext ctx, EntityMetadata<T> metadata, T entity) {
        update(ctx, metadata, singleton(entity));
    }

    @Override
    public <T extends Entity> void update(DatabaseContext ctx, EntityMetadata<T> metadata, Iterable<T> entities) {
        for (T entity : entities) {
            execute(dialect.update(metadata, entity));
        }
        commit();
    }

    @Override
    public <T extends Entity> void destroy(DatabaseContext ctx, EntityMetadata<T> metadata, T entity) {
        destroy(ctx, metadata, singleton(entity));
    }

    @Override
    public <T extends Entity> void destroy(DatabaseContext ctx, EntityMetadata<T> metadata, Iterable<T> entities) {
        for (T entity : entities) {
            execute(dialect.delete(metadata, entity));
        }
        commit();
    }
}
