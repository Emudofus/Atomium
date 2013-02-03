package org.atomium;

/**
 * a read-write repository that should be started and stopped manually
 *
 * @author blackrush
 */
public interface MutableRepository<T extends MutableEntity> extends Repository<T> {
    /**
     * @param entity entity's instance
     * @return <code>true</code> if entity's instance is persisted
     */
    boolean isPersisted(T entity);

    /**
     * insert or update SQL
     * @param entity entity's instance
     */
    void persist(T entity);

    /**
     * delete SQL
     * @param entity entity's instance
     */
    void delete(T entity);
}
