package org.atomium.annotations;

import org.atomium.metadata.ConverterInterface;

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

    String value() default DEFAULT;
    Class<? extends ConverterInterface> converter() default ConverterInterface.Invalid.class;
    boolean nullable() default false;
}
