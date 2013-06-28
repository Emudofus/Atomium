package org.atomium.metadata;

import com.google.common.reflect.TypeToken;
import org.atomium.NamedValues;

/**
 * @author Blackrush
 */
public interface ConverterInterface {
    /**
     * set the {@link MetadataRegistry} that this instance will be bound to
     * @param registry the non-null {@link MetadataRegistry} instance
     */
    void setMetadataRegistry(MetadataRegistry registry);

    /**
     * returns the {@link TypeToken} that this instance can extract
     * @return a non-null and non-empty array of {@link TypeToken}
     */
    TypeToken<?>[] getExtracted();

    /**
     * returns the {@link TypeToken} that this instance can export
     * @return a non-null and non-empty array of {@link TypeToken}
     */
    TypeToken<?>[] getExported();

    /**
     * this method will extract values from the database and convert it to the proper type
     * @param column entity's column that will be set
     * @param instance instance that will receive the values
     * @param input the values read from the database
     * @param <T> entity's type
     * @return <code>true</code> if this method has extracted any values, <code>false</code> otherwise
     */
    <T> boolean extract(ColumnMetadata<T> column, T instance, NamedValues input);

    /**
     * this method will export values from the entity to the proper database type
     * @param column enttiy's column that will be exported
     * @param instance instance that will have exported values
     * @param output the exported values
     * @param <T> entity's type
     * @return <code>true</code> if this method has exported any values, <code>false</code> otherwise
     */
    <T> boolean export(ColumnMetadata<T> column, T instance, NamedValues output);

    /**
     * useful for {@link org.atomium.annotations.Column}
     */
    public static final class Invalid implements ConverterInterface {
        private Invalid() {
            throw new IllegalAccessError();
        }

        @Override
        public void setMetadataRegistry(MetadataRegistry registry) {
            throw new IllegalAccessError();
        }

        @Override
        public TypeToken<?>[] getExtracted() {
            throw new IllegalAccessError();
        }

        @Override
        public TypeToken<?>[] getExported() {
            throw new IllegalAccessError();
        }

        @Override
        public <T> boolean extract(ColumnMetadata<T> column, T instance, NamedValues input) {
            throw new IllegalAccessError();
        }

        @Override
        public <T> boolean export(ColumnMetadata<T> column, T instance, NamedValues output) {
            throw new IllegalAccessError();
        }
    }
}
