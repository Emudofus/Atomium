package org.atomium;

import org.atomium.entity.EntityProperty;

/**
 * @author blackrush
 */
public interface TypeConverter {
    <T extends Entity> NamedValues export(DatabaseContext ctx, T entity, EntityProperty<T> property);
    <T extends Entity> Object extract(DatabaseContext ctx, T entity, EntityProperty<T> property, NamedValues raw);
}
