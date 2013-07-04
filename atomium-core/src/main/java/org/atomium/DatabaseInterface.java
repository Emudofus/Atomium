package org.atomium;

import org.atomium.metadata.MetadataRegistry;

/**
 * @author Blackrush
 */
public interface DatabaseInterface extends AutoCloseable {
    /**
     * load any external resources used by this database service
     */
    void load();

    /**
     * releases any external resources used by this database service
     */
    @Override
    void close();

    /**
     * get the {@link DialectInterface} used by the database
     * @return the non-null dialect
     */
    DialectInterface getDialect();

    /**
     * get the {@link org.atomium.metadata.MetadataRegistry} used to store the metadata
     * @return the non-null registry
     */
    MetadataRegistry getRegistry();

    SessionInterface createSession();

    /**
     * create a reference to the given model by its primary key
     * @param target entity's class
     * @param identifier primary key's value
     * @param <T> entity's type
     * @return the non-null reference
     * @throws IllegalArgumentException if {@code target} is not registered
     */
    <T> Ref<T> ref(Class<T> target, Object identifier);

    /**
     * create a reference to the given model by one of its column
     * @param target entity's class
     * @param column entity's column
     * @param identifier column's value
     * @param <T> entity's type
     * @return the non-null reference
     * @throws IllegalArgumentException if {@code target} is not registered or {@code target} hasn't any {@code column} value
     */
    <T> Ref<T> ref(Class<T> target, String column, Object identifier);
}
