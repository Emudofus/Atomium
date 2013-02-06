package org.atomium.persistence.dialect;

import com.google.common.collect.Lists;
import org.atomium.Entity;
import org.atomium.Query;
import org.atomium.entity.EntityMetadata;
import org.atomium.entity.EntityProperty;
import org.atomium.persistence.Dialect;

import java.util.List;

/**
 * @author blackrush
 */
public class MySqlDialect implements Dialect {
    private static String t(String item) {
        return "`" + item + "`";
    }

    @Override
    public <T extends Entity> Query insert(EntityMetadata<T> metadata, T entity) {
        boolean first;
        List<EntityProperty<T>> properties = metadata.getProperties();
        List<Object> parameters = Lists.newArrayList();
        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO ").append(t(metadata.getName())).append('(');

        first = true;
        for (EntityProperty<T> prop : properties) {
            if (first) first = false;
            else query.append(',');

            query.append(t(prop.getName()));
        }

        query.append(") VALUES (");

        first = true;
        for (EntityProperty<T> prop : properties) {
            if (first) first = false;
            else query.append(',');

            query.append('?');
            parameters.add(prop.get(entity));
        }

        return Query.create(query.append(");").toString(), parameters);
    }

    @Override
    public <T extends Entity> Query update(EntityMetadata<T> metadata, T entity) {
        boolean first;
        List<Object> parameters = Lists.newArrayList();
        StringBuilder query = new StringBuilder();

        query.append("UPDATE ").append(t(metadata.getName())).append(" SET ");

        first = true;
        for (EntityProperty<T> prop : metadata.getProperties()) {
            if (prop == metadata.getPrimaryKeyProperty()) continue;

            if (first) first = false;
            else query.append(',');

            query.append(t(prop.getName())).append("=?");
            parameters.add(prop.get(entity));
        }

        query.append(" WHERE ").append(t(metadata.getPrimaryKeyProperty().getName())).append("=?");

        parameters.add(metadata.getPrimaryKeyProperty().get(entity));

        return Query.create(query.append(';').toString(), parameters);
    }

    @Override
    public <T extends Entity> Query delete(EntityMetadata<T> metadata, T entity) {
        List<Object> parameters = Lists.newArrayList();
        StringBuilder query = new StringBuilder();

        query.append("DELETE FROM ").append(t(metadata.getName()))
             .append(" WHERE ").append(t(metadata.getPrimaryKeyProperty().getName())).append("=?");

        parameters.add(metadata.getPrimaryKeyProperty().get(entity));

        return Query.create(query.append(';').toString(), parameters);
    }

    @Override
    public <T extends Entity> Query select(EntityMetadata<T> metadata) {
        boolean first;
        StringBuilder query = new StringBuilder();

        query.append("SELECT ");

        first = true;
        for (EntityProperty<T> prop : metadata.getProperties()) {
            if (first) first = false;
            else query.append(',');

            query.append(t(prop.getName()));
        }

        query.append(" FROM ").append(t(metadata.getName())).append(';');

        return Query.create(query.toString());
    }
}
