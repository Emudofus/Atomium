package org.atomium.metadata;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import org.atomium.metadata.matchers.AnnotationConverterMatcher;
import org.atomium.metadata.matchers.TargetConverterMatcher;

import java.lang.annotation.Annotation;
import java.util.Collection;

import static java.util.Arrays.asList;

/**
 * @author Blackrush
 */
public final class ConverterMatchers {
    private ConverterMatchers() {}

    public static ConverterMatcher withTarget(Collection<TypeToken<?>> targets) {
        return TargetConverterMatcher.create(ImmutableSet.copyOf(targets));
    }

    public static ConverterMatcher withTarget(TypeToken<?>... targets) {
        return withTarget(asList(targets));
    }

    public static ConverterMatcher withAnnotations(Collection<Class<? extends Annotation>> annotationClasses) {
        return AnnotationConverterMatcher.create(ImmutableSet.copyOf(annotationClasses));
    }

    @SafeVarargs
    public static ConverterMatcher withAnnotations(Class<? extends Annotation>... annotationClasses) {
        return withAnnotations(asList(annotationClasses));
    }
}
