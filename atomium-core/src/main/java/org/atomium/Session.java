package org.atomium;

import com.google.common.collect.Maps;
import org.atomium.caches.CacheInterface;
import org.atomium.metadata.ColumnMetadata;
import org.atomium.metadata.Metadata;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Blackrush
 */
public abstract class Session implements SessionInterface {
    private final DatabaseInterface database;
    private final Map<Metadata<?>, CacheInterface<?>> caches = Maps.newHashMap();

    protected Session(DatabaseInterface database) {
        this.database = database;
    }

    protected abstract <T> CacheInterface<T> createCache(Metadata<T> metadata);

    @SuppressWarnings("unchecked")
    protected final <T> CacheInterface<T> cacheOf(Metadata<T> target) {
        return (CacheInterface<T>) caches.get(target);
    }

    @Override
    public DatabaseInterface getDatabase() {
        return database;
    }

    @Override
    public <T> CacheInterface<T> getCache(Class<T> target) {
        checkNotNull(target, "target");
        return getCache(database.getRegistry().get(target));
    }

    @Override
    public <T> CacheInterface<T> getCache(Metadata<T> target) {
        checkNotNull(target, "target");

        CacheInterface<T> cache = cacheOf(target);
        if (cache == null) {
            cache = createCache(target);
            caches.put(target, cache);
        }
        return cache;
    }

    @Override
    public <T> T findOne(Class<T> target, String columnName, Object value) {
        return findOne(database.getRegistry().get(target), columnName, value);
    }

    @Override
    public <T> T findOne(Metadata<T> target, String columnName, Object value) {
        return findOne(target.getColumn(columnName), value);
    }

    @Override
    public <T> T findOne(ColumnMetadata<T> column, Object value) {
        return findOne(column.getRef(value));
    }
}
