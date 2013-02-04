package org.atomium.entity;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import org.atomium.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author blackrush
 */
public class EntityProperty<T extends Entity> extends SimpleAttribute<T, Object> {
    private static Optional<Method> getMethod(Class<?> klass, String name) {
        while (klass != Object.class) {
            for (Method method : klass.getDeclaredMethods()) {
                if (method.getName().equalsIgnoreCase(name)) {
                    return Optional.of(method);
                }
            }
            klass = klass.getSuperclass();
        }

        return Optional.absent();
    }

    private static Optional<Method> getter(Class<?> klass, Field field, String propertyName) {
        Optional<Method> getter = getMethod(klass, "get" + field.getName());
        if (getter.isPresent()) {
            return getter; // can use Optional.or()
        }

        // assume that name is lowerUnderscore'd so convert it to lowerCamel
        return getMethod(field.getDeclaringClass(), "get" + propertyName.replaceAll("_", ""));
    }

    private static Optional<Method> setter(Class<?> klass, Field field, String propertyName) {
        Optional<Method> setter = getMethod(klass, "set" + field.getName());
        if (setter.isPresent()) {
            return setter; // can use Optional.or()
        }

        // assume that name is lowerUnderscore'd so convert it to lowerCamel
        return getMethod(klass, "set" + propertyName.replaceAll("_", ""));
    }

    private final EntityMetadata<T> metadata;
    private final String name;
    private final boolean mutable;
    private final Method getter, setter;

    @SuppressWarnings("unchecked") // fucking generics
    public EntityProperty(EntityMetadata<T> metadata, Field field, String name, boolean mutable) {
        super(metadata.getEntityClass(), (Class<Object>) field.getType(), name);

        // this class is instantiated in the EntityMetadata's ctor so be careful !

        this.metadata = checkNotNull(metadata);
        this.name = name;
        this.mutable = mutable;

        // safe because EntityMetadata's entity class has been initialized
        this.getter = getter(metadata.getEntityClass(), field, name).get();
        this.setter = mutable ?
                setter(metadata.getEntityClass(), field, name).get() :
                null;
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
     * @return <code>true</code> if this property is mutable
     */
    public boolean isMutable() {
        return mutable;
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
        checkState(mutable, "you can't set an immutable property");

        try {
            setter.invoke(entity, value);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
