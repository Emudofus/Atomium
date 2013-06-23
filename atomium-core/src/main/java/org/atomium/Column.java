package org.atomium;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Blackrush
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Column {
    static final String DEFAULT = "/!\\ NEVER USE THIS STRING /!\\";

    static final class DEFAULT implements ConverterInterface {
        private DEFAULT() {
            throw new IllegalAccessError();
        }
        public <T> boolean extract(ColumnMetadata<T> column, T instance, NamedValues input) {
            throw new IllegalAccessError();
        }
        public <T> boolean export(ColumnMetadata<T> column, T instance, NamedValues output) {
            throw new IllegalAccessError();
        }
    }

    String value() default DEFAULT;
    Class<? extends ConverterInterface> converter() default DEFAULT.class;
    boolean nullable() default false;
}
