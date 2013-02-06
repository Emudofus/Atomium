package org.atomium.entity;

import com.google.common.base.Optional;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import org.atomium.Entity;
import org.atomium.TypeConverter;
import org.atomium.annotation.Column;
import org.atomium.annotation.TypeConverterAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Throwables.propagate;
import static org.atomium.entity.EntityMetadata.lowerCamelToLowerUnderscore;

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

    private static Optional<Class<? extends TypeConverter>> findTypeConverter(Field field) {
        for (Annotation annotation : field.getAnnotations()) {
            TypeConverterAnnotation typeConverterAnnotation = annotation.annotationType().getAnnotation(TypeConverterAnnotation.class);
            if (typeConverterAnnotation == null) continue;

            return Optional.of((Class<? extends TypeConverter>) typeConverterAnnotation.value());
        }

        return Optional.absent();
    }

    public static <T extends Entity> EntityProperty<T> of(EntityMetadata<T> metadata, Field field) {
        String name;
        boolean mutable;
        Optional<Class<? extends TypeConverter>> typeConverter;

        Column annotation = field.getAnnotation(Column.class);
        if (annotation != null) {
            name = annotation.value();
            mutable = annotation.mutable();
            typeConverter = annotation.typeConverter() != Column.DEFAULT_TYPE_CONVERTER
                    ? Optional.of((Class<? extends TypeConverter>) annotation.typeConverter())
                    : findTypeConverter(field);
        } else {
            name = lowerCamelToLowerUnderscore(field.getName());
            mutable = true; // FIXME should be false or true by default ?
            typeConverter = findTypeConverter(field);
        }

        return new EntityProperty<T>(metadata, field, name, mutable, typeConverter, getter(metadata.getEntityClass(), field, name).get(), setter(metadata.getEntityClass(), field, name).get());
    }

    private final EntityMetadata<T> metadata;
    private final Field field;
    private final String name;
    private final boolean mutable;
    private final Optional<Class<? extends TypeConverter>> typeConverter;
    private final Method getter, setter;

    @SuppressWarnings("unchecked")
    protected EntityProperty(EntityMetadata<T> metadata, Field field, String name, boolean mutable, Optional<Class<? extends TypeConverter>> typeConverter, Method getter, Method setter) {
        super(metadata.getEntityClass(), (Class<Object>) field.getType(), name);
        this.metadata = metadata;
        this.field = field;
        this.name = name;
        this.mutable = mutable;
        this.typeConverter = typeConverter;
        this.getter = getter;
        this.setter = setter;
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
     * @return this property's converter
     */
    public Optional<Class<? extends TypeConverter>> getTypeConverter() {
        return typeConverter;
    }

    /**
     * @return property's class
     */
    public Class<?> getPropertyClass() {
        return getter.getReturnType();
    }

    @Override
    public Object getValue(T object) {
        return get(object);
    }

    /**
     * return entity property's value
     * @param entity entity's instance
     * @return property's value
     */
    public Object get(T entity) {
        try {
            return getter.invoke(entity);
        } catch (Exception e) {
            throw propagate(e);
        }
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
            throw propagate(e);
        }
    }

    public <T extends Annotation> T getAnnotation(Class<T> klass) {
        return field.getAnnotation(klass);
    }

    public <T extends Annotation> boolean isAnnotationPresent(Class<T> klass) {
        return field.isAnnotationPresent(klass);
    }
}
