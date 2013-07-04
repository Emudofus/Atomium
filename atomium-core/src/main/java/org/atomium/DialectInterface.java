package org.atomium;

import com.googlecode.cqengine.query.Query;
import org.atomium.metadata.Metadata;

/**
 * @author Blackrush
 */
public interface DialectInterface {
    /**
     * create a query that will build structure on the database
     * @param meta entity's metadata used to build structure
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface buildStructure(Metadata<T> meta);

    /**
     * create a query that will destroy structure from the database
     * @param meta entity's metadata used to destroy structure
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface destroyStructure(Metadata<T> meta);

    /**
     * create a "create" query that will insert data into the database
     * @param meta entity's metadata
     * @param instance data to insert
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface create(Metadata<T> meta, T instance);

    /**
     * create a "read" query that will fetch data from the database
     * @param meta entity's metadata
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface read(Metadata<T> meta);

    /**
     * create a "read" query that will fetch data from the database according to the given criteria
     * if {@code criteria} is null, this method is equivalent to {@link #read(org.atomium.metadata.Metadata)}
     * @param meta entity's metadata
     * @param criteria the nullable criteria
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface read(Metadata<T> meta, Query<T> criteria);

    /**
     * create a "read" query that will fetch a single row from the database
     * @param ref entity's reference
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface read(Ref<T> ref);

    /**
     * create an "update" query that will persist data into the database
     * @param meta entity's metadata
     * @param instance data to persist
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface update(Metadata<T> meta, T instance);

    /**
     * create a "delete" query that will delete a single row from the database
     * @param meta entity's metadata
     * @param instance data to delete
     * @param <T> entity's type
     * @return created query
     */
    <T> QueryInterface delete(Metadata<T> meta, T instance);

    /**
     * create a "delete" query that will delete a single row from the database
     *
     * @param ref reference to the data to delete
     * @return created query
     */
    <T> QueryInterface delete(Ref<T> ref);
}
