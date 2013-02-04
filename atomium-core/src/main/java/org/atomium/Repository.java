package org.atomium;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.atomium.pkey.PrimaryKey;

/**
 * a read-only repository that should be started and stopped manually
 *
 * @author blackrush
 */
public interface Repository<T extends Entity> {
    /**
     * @return entity's class
     */
    Class<T> getEntityClass();

    /**
     * should be invoked on app's start
     * @param context repository's context
     */
    void setUp(DatabaseContext context);

    /**
     * should be invoked on app's stop
     */
    void tearDown();

    /**
     * find an entity by its primaryKey
     * @param key primaryKey
     * @return found entity
     */
    Optional<T> find(PrimaryKey key);

    /**
     * find an entity by one of its property
     * @param property property's name
     * @param obj value
     * @return found entity
     */
    Optional<T> find(String property, Object obj);

    /**
     * find an entity with a predicate
     * @param predicate predicate
     * @return found entity
     */
    Optional<T> find(Predicate<T> predicate);

    /**
     * filter all entities by one of their property
     * @param property property's name
     * @param obj value
     * @return found entities
     */
    Iterable<T> filter(String property, Object obj);

    /**
     * filter all entities with a predicate
     * @param predicate predicate
     * @return found entities
     */
    Iterable<T> filter(Predicate<T> predicate);
}
