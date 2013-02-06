package org.atomium;

import org.atomium.entity.EntityProperty;

import java.util.Map;

/**
 * @author blackrush
 */
public interface TypeConverter {
    <T extends Entity> Map<String, Object> export(DatabaseContext ctx, T entity, EntityProperty<T> property);
    <T extends Entity> Object extract(DatabaseContext ctx, T entity, EntityProperty<T> property, Map<String, Object> raw);
}
