package org.atomium.annotations;

import org.atomium.CacheType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Blackrush
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    static final String DEFAULT = "/!\\ NEVER USE THIS STRING /!\\";

    String value() default DEFAULT;

    CacheType cache() default CacheType.NONE;
}
