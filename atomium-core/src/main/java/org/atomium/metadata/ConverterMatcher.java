package org.atomium.metadata;

/**
 * @author Blackrush
 */
public interface ConverterMatcher {
    <T> boolean matches(ColumnMetadata<T> column);
}
