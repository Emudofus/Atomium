package org.atomium;

import com.googlecode.cqengine.query.Query;
import org.atomium.caches.CacheInterface;
import org.atomium.metadata.ColumnMetadata;
import org.atomium.metadata.Metadata;

import java.util.Set;

/**
 * @author Blackrush
 */
public interface SessionInterface extends AutoCloseable {
    @Override
    void close();

    DatabaseInterface getDatabase();

    <T> CacheInterface<T> getCache(Class<T> target);
    <T> CacheInterface<T> getCache(Metadata<T> target);

    <T> T findOne(Class<T> target, String columnName, Object value);
    <T> T findOne(Metadata<T> target, String columnName, Object value);
    <T> T findOne(ColumnMetadata<T> column, Object value);
    <T> T findOne(Ref<T> ref);

    <T> Set<T> find(Class<T> target, Query<T> query);
    <T> Set<T> find(Metadata<T> target, Query<T> query);
    <T> Set<T> all(Class<T> target);
    <T> Set<T> all(Metadata<T> target);

    <T> void persist(T instance);

    <T> void remove(T instance);
    <T> boolean remove(Ref<T> ref);
}
