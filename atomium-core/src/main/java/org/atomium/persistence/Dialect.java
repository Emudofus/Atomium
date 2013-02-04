package org.atomium.persistence;

import org.atomium.Entity;
import org.atomium.entity.EntityMetadata;

/**
 * @author blackrush
 */
public interface Dialect {
    /**
     * build a query that will insert data in the database
     * @param entity list of data to insert
     * @param metadata data's metadata
     * @param <T> data's type
     * @return query
     */
    <T extends Entity> String insert(Iterable<T> entity, EntityMetadata<T> metadata);

    /**
     * build a query that will update data in the database
     * @param entity list of data to update
     * @param metadata data's metadata
     * @param <T> data's type
     * @return query
     */
    <T extends Entity> String update(Iterable<T> entity, EntityMetadata<T> metadata);

    /**
     * build a query that will delete data in the database
     * @param entity list of data to delete
     * @param metadata data's metadata
     * @param <T> data's type
     * @return query
     */
    <T extends Entity> String delete(Iterable<T> entity, EntityMetadata<T> metadata);

    /**
     * build a query that will fetch data from the database
     * @param metadata data's metadata
     * @param <T> data's type
     * @return query
     */
    <T extends Entity> String select(EntityMetadata<T> metadata);
}
