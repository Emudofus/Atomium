package org.atomium;

import org.atomium.entity.EntityMetadata;

/**
 * CRUD strategy
 * @author blackrush
 */
public interface PersistenceStrategy {
    void setUp();
    void tearDown();

    void setImplicitTypeConverter(TypeConverter typeConverter);

    <T extends Entity> void create(DatabaseContext ctx, EntityMetadata<T> metadata, T entity);
    <T extends Entity> void create(DatabaseContext ctx, EntityMetadata<T> metadata, Iterable<T> entity);

    <T extends Entity> Iterable<T> read(DatabaseContext ctx, EntityMetadata<T> metadata);

    <T extends Entity> void update(DatabaseContext ctx, EntityMetadata<T> metadata, T entity);
    <T extends Entity> void update(DatabaseContext ctx, EntityMetadata<T> metadata, Iterable<T> entity);

    <T extends Entity> void destroy(DatabaseContext ctx, EntityMetadata<T> metadata, T entity);
    <T extends Entity> void destroy(DatabaseContext ctx, EntityMetadata<T> metadata, Iterable<T> entity);
}
