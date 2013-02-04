package org.atomium.persistence.dialect;

import org.atomium.Entity;
import org.atomium.entity.EntityMetadata;
import org.atomium.entity.EntityProperty;
import org.atomium.persistence.Dialect;

/**
 * @author blackrush
 */
public class MySqlDialect implements Dialect {
    private static String t(String item) {
        return "`" + item + "`";
    }

    private static String q(String item) {
        return "'" + item + "'";
    }

    @Override
    public <T extends Entity> String insert(Iterable<T> entities, EntityMetadata<T> metadata) {
        boolean first, first2;
        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO ").append(t(metadata.getName()));

        query.append('(');

        first = true;
        for (EntityProperty<T> prop : metadata.getProperties()) {
            if (first) first = false;
            else query.append(',');

            query.append(t(prop.getName()));
        }

        query.append(')');

        query.append(" VALUES ");

        first = true;
        for (T entity : entities) {
            if (first) first = false;
            else query.append(',');

            query.append('(');

            first2 = true;
            for (EntityProperty<T> prop : metadata.getProperties()) {
                if (first2) first2 = false;
                else query.append(',');

                Object value = prop.get(entity);
                if (value != null) {
                    query.append(q(value.toString())); // TODO
                } else {
                    query.append("NULL");
                }
            }

            query.append(')');
        }

        query.append(';');

        return query.toString();
    }

    @Override
    public <T extends Entity> String update(Iterable<T> entities, EntityMetadata<T> metadata) {
        boolean first;
        StringBuilder query = new StringBuilder();

        for (T entity : entities) {
            query.append("UPDATE ").append(t(metadata.getName())).append(" SET ");

            first = true;
            for (EntityProperty<T> prop : metadata.getProperties()) {
                if (prop == metadata.getPrimaryKeyProperty()) continue;

                if (first) first = false;
                else query.append(',');

                Object value = prop.get(entity);

                query.append(t(prop.getName())).append('=').append(value == null ? "NULL" : q(value.toString()));
            }

            query.append(" WHERE ").append(t(metadata.getPrimaryKeyProperty().getName()))
                 .append('=').append(q(metadata.getPrimaryKeyProperty().get(entity).toString()))
                 .append(';');
        }

        return query.toString();
    }

    @Override
    public <T extends Entity> String delete(Iterable<T> entities, EntityMetadata<T> metadata) {
        boolean first;
        StringBuilder query = new StringBuilder();

        query.append("DELETE FROM ").append(t(metadata.getName()))
             .append(" WHERE ").append(t(metadata.getPrimaryKeyProperty().getName()));

        first = true;
        for (T entity : entities) {
            if (first) first = false;
            else query.append(" OR ").append(t(metadata.getPrimaryKeyProperty().getName()));

            query.append('=')
                 .append(q(metadata.getPrimaryKeyProperty().get(entity).toString()));
        }

        return query.append(';').toString();
    }

    @Override
    public <T extends Entity> String select(EntityMetadata<T> metadata) {
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

        return query.toString();
    }
}
