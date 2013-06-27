package org.atomium;

import org.atomium.metadata.MetadataRegistry;

import java.util.Set;

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

    /**
     * find an entity by its primary key
     * @param target entity's class
     * @param identifier primary key value
     * @param <T> entity's type
     * @return the non-null found entity
     * @throws DatabaseException.NotFound if there is no result
     * @throws DatabaseException.NonUnique if there are more than one result
     * @throws IllegalArgumentException if {@code target} is not registered
     * @see #findOne(Ref)
     */
    <T> T findOne(Class<T> target, Object identifier);

    /**
     * find an entity by one of its column
     * @param target entity's class
     * @param column column's name
     * @param value value
     * @param <T> entity's type
     * @return the non-null found entity
     * @throws DatabaseException.NotFound if there is no result
     * @throws DatabaseException.NonUnique if there are more than one result
     * @throws IllegalArgumentException if {@code target} is not registered or {@code target} hasn't any {@code column} value
     * @see #findOne(Ref)
     */
    <T> T findOne(Class<T> target, String column, Object value);

    /**
     * find an entity by its reference
     * @param ref entity's reference
     * @param <T> entity's type
     * @return the non-null found entity
     * @throws DatabaseException.NotFound if there is no result
     * @throws DatabaseException.NonUnique if there are more than one result
     * @see DialectInterface#read(Ref)
     * @see org.atomium.metadata.Metadata#map(NamedValues)
     */
    <T> T findOne(Ref<T> ref);

    /**
     * find all entities from the database
     * @param target entity's class
     * @param <T> entity's type
     * @return the non-null result set
     * @throws DatabaseException.NotFound if there is no result
     * @throws IllegalArgumentException if {@code target} is not registered
     * @see DialectInterface#read(org.atomium.metadata.Metadata)
     */
    <T> Set<T> all(Class<T> target);

    /**
     * create or update the given entity on the database
     * set the primary key if marked as auto-generated
     * @param instance the entity instance
     * @param <T> the entity's type
     */
    <T> void persist(T instance);

    /**
     * delete the given entity from the database
     * it will ignore this request if the given entity is not persisted
     * @param instance the entity instance
     * @param <T> the entity's type
     */
    <T> void remove(T instance);

    /**
     * delete an entity from the database by its refenrece
     * @param ref the entity reference
     * @param <T> the entity's type
     * @return {@code} true if it successfully deleted the entity
     */
    <T> boolean remove(Ref<T> ref);
}
