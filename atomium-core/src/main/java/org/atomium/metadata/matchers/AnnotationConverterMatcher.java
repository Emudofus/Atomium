package org.atomium.metadata.matchers;

import org.atomium.metadata.ColumnMetadata;
import org.atomium.metadata.ConverterMatcher;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Blackrush
 */
public final class AnnotationConverterMatcher implements ConverterMatcher {
    private final Set<Class<? extends Annotation>> annotationClasses;

    private AnnotationConverterMatcher(Set<Class<? extends Annotation>> annotationClasses) {
        this.annotationClasses = annotationClasses;
    }

    public static AnnotationConverterMatcher create(Set<Class<? extends Annotation>> annotationClasses) {
        return new AnnotationConverterMatcher(annotationClasses);
    }

    @Override
    public <T> boolean matches(ColumnMetadata<T> column) {
        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            Annotation annotation = column.getAnnotation(annotationClass);
            if (annotation != null) {
                return true;
            }
        }
        return false;
    }
}
