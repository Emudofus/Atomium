package org.atomium;

import org.atomium.entity.EntityProperty;

/**
 * @author blackrush
 */
public interface TypeConverter {
    Class<?> getTargetClass();

    <T extends Entity> void export(DatabaseContext ctx, T entity, EntityProperty<T> property, NamedValues raw);
    <T extends Entity> Object extract(DatabaseContext ctx, T entity, EntityProperty<T> property, NamedValues raw);
}
