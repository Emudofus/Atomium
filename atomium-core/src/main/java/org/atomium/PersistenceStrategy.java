package org.atomium;

import org.atomium.entity.EntityMetadata;

/**
 * CRUD strategy
 * @author blackrush
 */
public interface PersistenceStrategy {
    <T extends Entity> void create(T entity, EntityMetadata<T> metadata);
    <T extends Entity> void create(Iterable<T> entity, EntityMetadata<T> metadata);

    <T extends Entity> Iterable<T> read(EntityMetadata<T> metadata);

    <T extends Entity> void update(T entity, EntityMetadata<T> metadata);
    <T extends Entity> void update(Iterable<T> entity, EntityMetadata<T> metadata);

    <T extends Entity> void destroy(T entity, EntityMetadata<T> metadata);
    <T extends Entity> void destroy(Iterable<T> entity, EntityMetadata<T> metadata);
}
