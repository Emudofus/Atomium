package org.atomium.metadata;

import com.google.common.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;

/**
 * @author Blackrush
 */
public class FieldColumnMetadata<T> extends ColumnMetadata<T> {
    private final Field field;

    public FieldColumnMetadata(Metadata<T> parent, String name, Field field) {
        super(parent, name, TypeToken.of(field.getGenericType()));
        this.field = checkNotNull(field, "field");
        this.field.setAccessible(true);
    }

    @Override
    public Object get(T instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw propagate(e);
        }
    }

    @Override
    public void set(T instance, Object o) {
        try {
            field.set(instance, o);
        } catch (IllegalAccessException e) {
            throw propagate(e);
        }
    }

    @Override
    public <T1 extends Annotation> T1 getAnnotation(Class<T1> clazz) {
        return field.getAnnotation(clazz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldColumnMetadata that = (FieldColumnMetadata) o;

        if (!field.equals(that.field)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return field.hashCode();
    }
}
