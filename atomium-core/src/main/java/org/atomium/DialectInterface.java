package org.atomium;

/**
 * @author Blackrush
 */
public interface DialectInterface {

    <T> QueryInterface buildStructure(Metadata<T> meta);

    <T> QueryInterface destroyStructure(Metadata<T> meta);

    /**
     * create a "create" statement that will insert data into the database
     * @param meta entity's metadata
     * @param instance data to insert
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface create(Metadata<T> meta, T instance);

    /**
     * create a "read" statement that will fetch data from the database
     * @param meta entity's metadata
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface read(Metadata<T> meta);

    /**
     * create a "read" statement that will fetch a single row from the database
     * @param ref entity's reference
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface read(Ref<T> ref);

    /**
     * create an "update" statement that will persist data into the database
     * @param meta entity's metadata
     * @param instance data to persist
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface update(Metadata<T> meta, T instance);

    /**
     * create a "delete" statement that will delete a single row from the database
     * @param meta entity's metadata
     * @param instance data to delete
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface delete(Metadata<T> meta, T instance);

    /**
     * create a "delete" statement that will delete a single row from the database
     *
     * @param ref reference to the data to delete
     * @return created query
     */
    <T> QueryInterface delete(Ref<T> ref);
}
