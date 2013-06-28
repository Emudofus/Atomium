package org.atomium.metadata;

import com.google.common.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.google.common.base.Throwables.propagate;

/**
 * @author Blackrush
 */
public class FieldColumnMetadata<T> extends ColumnMetadata<T> {
    private final Field field;

    public FieldColumnMetadata(Metadata<T> parent, String name, Field field) {
        super(parent, name);
        this.field = field;
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
    public TypeToken<?> getTarget() {
        return TypeToken.of(field.getGenericType());
    }
}
