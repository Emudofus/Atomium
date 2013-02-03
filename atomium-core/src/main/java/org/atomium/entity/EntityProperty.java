package org.atomium.entity;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import org.atomium.Entity;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author blackrush
 */
public class EntityProperty<T extends Entity> extends SimpleAttribute<T, Object> {
    private static Optional<Method> getter(Class<?> klass, String name) {
        name = "get" + name.replaceAll("_", ""); // assume that name is lowerUnderscore'd so convert it to lowerCamel

        for (Method method : klass.getMethods()) {
            if (method.getName().equalsIgnoreCase(name)) {
                return Optional.of(method);
            }
        }

        return Optional.absent();
    }

    private static Optional<Method> setter(Class<?> klass, String name) {
        name = "set" + name.replaceAll("_", ""); // assume that name is lowerUnderscore'd so convert it to lowerCamel

        for (Method method : klass.getMethods()) {
            if (method.getName().equalsIgnoreCase(name)) {
                return Optional.of(method);
            }
        }

        return Optional.absent();
    }

    private final EntityMetadata<T> metadata;
    private final String name;
    private final Method getter, setter;

    public EntityProperty(EntityMetadata<T> metadata, String name) {
        // this class is instantiated in the EntityMetadata's ctor so be careful !

        this.metadata = checkNotNull(metadata);
        this.name = name;

        // safe because EntityMetadata's entity class has been initialized
        this.getter = getter(metadata.getEntityClass(), name).get();
        this.setter = setter(metadata.getEntityClass(), name).get();
    }

    /**
     * @return non-null metadata
     */
    public EntityMetadata<T> getMetadata() {
        return metadata;
    }

    /**
     * @return property's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return property's class
     */
    public Class<?> getPropertyClass() {
        return getter.getReturnType();
    }

    @Override
    public Object getValue(T object) {
        try {
            return getter.invoke(object);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * return entity property's value
     * @param entity entity's instance
     * @return property's value
     */
    public Object get(T entity) {
        return getValue(entity);
    }

    /**
     * set entity property's value
     * @param entity entity's instance
     * @param value new value
     */
    public void set(T entity, Object value) {
        try {
            setter.invoke(entity, value);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
