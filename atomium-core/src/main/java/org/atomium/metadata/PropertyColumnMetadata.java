package org.atomium.metadata;

import com.google.common.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.google.common.base.Throwables.propagate;

/**
 * @author Blackrush
 */
public class PropertyColumnMetadata<T> extends ColumnMetadata<T> {
    private final Method getter, setter;

    public PropertyColumnMetadata(Metadata<T> parent, String name, Method getter, Method setter) {
        super(parent, name);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public Object get(T instance) {
        try {
            return getter.invoke(instance);
        } catch (IllegalAccessException e) {
            throw propagate(e);
        } catch (InvocationTargetException e) {
            throw propagate(e.getTargetException());
        }
    }

    @Override
    public void set(T instance, Object o) {
        try {
            setter.invoke(instance, o);
        } catch (IllegalAccessException e) {
            throw propagate(e);
        } catch (InvocationTargetException e) {
            throw propagate(e.getTargetException());
        }
    }

    @Override
    public <T1 extends Annotation> T1 getAnnotation(Class<T1> clazz) {
        return getter.getAnnotation(clazz);
    }

    @Override
    public TypeToken<?> getTarget() {
        return TypeToken.of(getter.getGenericReturnType());
    }
}
