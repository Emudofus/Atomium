package org.atomium.persistence.dialect;

import com.google.common.collect.Lists;
import org.atomium.Entity;
import org.atomium.NamedValues;
import org.atomium.Query;
import org.atomium.entity.EntityMetadata;
import org.atomium.entity.EntityProperty;
import org.atomium.persistence.Dialect;

import java.util.List;
import java.util.Map;

/**
 * @author blackrush
 */
public class MySqlDialect implements Dialect {
    private static String t(String item) {
        return "`" + item + "`";
    }

    @Override
    public <T extends Entity> Query insert(EntityMetadata<T> metadata, NamedValues namedValues) {
        boolean first;
        Map<String, Object> values = namedValues.toMap();
        List<Object> parameters = Lists.newArrayList();
        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO ").append(t(metadata.getName())).append('(');

        first = true;
        for (String columnName : values.keySet()) {
            if (first) first = false;
            else query.append(',');

            query.append(t(columnName));
        }

        query.append(") VALUES (");

        first = true;
        for (Object value : values.values()) {
            if (first) first = false;
            else query.append(',');

            query.append('?');
            parameters.add(value);
        }

        return Query.create(query.append(");").toString(), parameters);
    }

    @Override
    public <T extends Entity> Query update(EntityMetadata<T> metadata, NamedValues values) {
        EntityProperty<T> pk = metadata.getPrimaryKeyProperty();
        boolean first;
        List<Object> parameters = Lists.newArrayList();
        StringBuilder query = new StringBuilder();

        query.append("UPDATE ").append(t(metadata.getName())).append(" SET ");

        first = true;
        for (Map.Entry<String, Object> entry : values.toMap().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(pk.getName())) continue;

            if (first) first = false;
            else query.append(',');

            query.append(t(entry.getKey())).append("=?");
            parameters.add(entry.getValue());
        }

        query.append(" WHERE ").append(t(pk.getName())).append("=?");

        parameters.add(values.get(pk.getName()));

        return Query.create(query.append(';').toString(), parameters);
    }

    @Override
    public <T extends Entity> Query delete(EntityMetadata<T> metadata, NamedValues values) {
        EntityProperty<T> pk = metadata.getPrimaryKeyProperty();
        List<Object> parameters = Lists.newArrayList();
        StringBuilder query = new StringBuilder();

        query.append("DELETE FROM ").append(t(metadata.getName()))
             .append(" WHERE ").append(t(pk.getName())).append("=?");

        parameters.add(values.get(pk.getName()));

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
