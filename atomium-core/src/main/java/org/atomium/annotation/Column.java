package org.atomium.annotation;

import org.atomium.DatabaseContext;
import org.atomium.Entity;
import org.atomium.NamedValues;
import org.atomium.TypeConverter;
import org.atomium.entity.EntityProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author blackrush
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String value();
    boolean mutable() default true;
    Class<? extends TypeConverter> typeConverter() default DEFAULT_TYPE_CONVERTER;

    public static final Class<? extends TypeConverter> DEFAULT_TYPE_CONVERTER = DefaultTypeConverter.class;
    final static class DefaultTypeConverter implements TypeConverter {
        private DefaultTypeConverter() {
            throw new UnsupportedOperationException();
        }

        public Class<?> getTargetClass() {
            throw new UnsupportedOperationException();
        }

        public <T extends Entity> void export(DatabaseContext ctx, T entity, EntityProperty<T> property, NamedValues raw) {
            throw new UnsupportedOperationException();
        }

        public <T extends Entity> Object extract(DatabaseContext ctx, T entity, EntityProperty<T> property, NamedValues raw) {
            throw new UnsupportedOperationException();
        }
    }
}
