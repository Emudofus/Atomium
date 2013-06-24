package org.atomium;

/**
 * @author Blackrush
 */
public interface DatabaseInterface extends AutoCloseable {
    void load();

    @Override
    void close();

    /**
     * get the {@link DialectInterface} used by the database
     * @return the non-null dialect
     */
    DialectInterface getDialect();

    /**
     * get the {@link MetadataRegistry} used to store the metadata
     * @return the non-null registry
     */
    MetadataRegistry getRegistry();

    /**
     * find an entity by its primary key
     * @param target entity's class
     * @param identifier primary key value
     * @param <T> entity's type
     * @return the non-null found entity
     * @throws DatabaseException.NotFound if there is no result
     * @throws DatabaseException.NonUnique if there are more than one result
     * @see #findOne(Ref)
     */
    <T> T findOne(Class<T> target, Object identifier);

    /**
     * find an entity by one of its column
     * @param target entity's class
     * @param name column's name
     * @param value value
     * @param <T> entity's type
     * @return the non-null found entity
     * @throws DatabaseException.NotFound if there is no result
     * @throws DatabaseException.NonUnique if there are more than one result
     * @see #findOne(Ref)
     */
    <T> T findOne(Class<T> target, String name, Object value);

    /**
     * find an entity by its reference
     * @param ref entity's reference
     * @param <T> entity's type
     * @return the non-null found entity
     * @throws DatabaseException.NotFound if there is no result
     * @throws DatabaseException.NonUnique if there are more than one result
     * @see DialectInterface#read(Ref)
     * @see Metadata#map(NamedValues)
     */
    <T> T findOne(Ref<T> ref);
}
