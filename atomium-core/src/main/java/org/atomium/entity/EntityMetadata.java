package org.atomium.entity;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.atomium.Entity;
import org.atomium.annotation.Column;
import org.atomium.annotation.Ignore;
import org.atomium.annotation.Table;
import org.atomium.pkey.PrimaryKey;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author blackrush
 */
public class EntityMetadata<T extends Entity> {
    public static <T extends Entity> EntityMetadata<T> of(Class<T> klass) {
        return new EntityMetadata<T>(klass);
    }

    private static String lowerCamelToLowerUnderscore(String string) {
        return string.replaceAll("([a-z]+)([A-Z])", "$1_$2").toLowerCase();
    }

    private static String getEntityName(Class<?> klass) {
        Table annotation = klass.getAnnotation(Table.class);

        if (annotation == null) {
            return lowerCamelToLowerUnderscore(klass.getSimpleName());
        } else {
            return annotation.value();
        }
    }

    private static <T extends Entity> EntityProperty<T> buildProperty(EntityMetadata<T> metadata, Field field) {
        Column annotation = field.getAnnotation(Column.class);

        String name;
        boolean mutable;
        if (annotation == null) {
            name = lowerCamelToLowerUnderscore(field.getName());
            mutable = true; // FIXME should be false ?
        } else {
            name = annotation.value();
            mutable = annotation.mutable();
        }

        return new EntityProperty<T>(metadata, field, name, mutable);
    }

    private static <T extends Entity> Map<String, EntityProperty<T>> findProperties(EntityMetadata<T> metadata, Class<T> klass) {
        Map<String, EntityProperty<T>> result = Maps.newHashMap();

        Class<? super T> clazz = klass;
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Ignore.class)) continue;

                EntityProperty<T> property = buildProperty(metadata, field);
                result.put(property.getName(), property);
            }

            clazz = clazz.getSuperclass();
        }

        return result;
    }

    private final Class<T> klass;
    private final String name;
    private final Map<String, EntityProperty<T>> properties;
    private final EntityProperty<T> pkProperty;

    public EntityMetadata(Class<T> klass) {
        this.klass = klass;
        this.name = getEntityName(klass);
        this.properties = ImmutableMap.copyOf(findProperties(this, klass)); // unsafe because of circular reference
        this.pkProperty = getProperty(PrimaryKey.class).get();
    }

    /**
     * @return entity's class
     */
    public Class<T> getEntityClass() {
        return klass;
    }

    /**
     * @return entity's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return an empty entity instance
     */
    public T create() {
        try {
            return getEntityClass().newInstance();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public EntityProperty<T> getPrimaryKeyProperty() {
        return pkProperty;
    }

    public List<EntityProperty<T>> getProperties() {
        return ImmutableList.copyOf(properties.values());
    }

    public Optional<EntityProperty<T>> getProperty(String name) {
        return Optional.fromNullable(properties.get(name));
    }

    public Optional<EntityProperty<T>> getProperty(Class<?> propertyClass) {
        for (EntityProperty<T> property : properties.values()) {
            if (propertyClass.isAssignableFrom(property.getPropertyClass())) {
                return Optional.of(property);
            }
        }

        return Optional.absent();
    }
}
