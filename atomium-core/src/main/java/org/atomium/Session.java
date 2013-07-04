package org.atomium;

import com.google.common.collect.Maps;
import com.googlecode.cqengine.query.Query;
import org.atomium.caches.CacheInterface;
import org.atomium.metadata.ColumnMetadata;
import org.atomium.metadata.Metadata;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
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

    protected <T> Metadata<T> metadataOf(Class<T> target) {
        Metadata<T> meta = database.getRegistry().get(target);
        checkArgument(meta != null, "%s must be registered", target);
        return meta;
    }

    protected <T> Metadata<T> metadataOf(T instance) {
        Metadata<T> meta = database.getRegistry().get(instance);
        checkArgument(meta != null, "%s must be registered", instance.getClass());
        return meta;
    }

    protected abstract <T> CacheInterface<T> createCache(Metadata<T> metadata);

    @SuppressWarnings("unchecked")
    protected final <T> CacheInterface<T> cacheOf(Metadata<T> target) {
        return (CacheInterface<T>) caches.get(checkNotNull(target, "target"));
    }

    @Override
    public DatabaseInterface getDatabase() {
        return database;
    }

    @Override
    public <T> CacheInterface<T> getCache(Class<T> target) {
        return getCache(metadataOf(target));
    }

    @Override
    public <T> CacheInterface<T> getCache(Metadata<T> target) {
        CacheInterface<T> cache = cacheOf(target);
        if (cache == null) {
            cache = createCache(target);
            caches.put(target, cache);
        }
        return cache;
    }

    @Override
    public <T> T findOne(Class<T> target, String columnName, Object value) {
        return findOne(metadataOf(target), columnName, value);
    }

    @Override
    public <T> T findOne(Metadata<T> target, String columnName, Object value) {
        return findOne(target.getColumn(columnName), value);
    }

    @Override
    public <T> T findOne(ColumnMetadata<T> column, Object value) {
        return findOne(column.getRef(value));
    }

    @Override
    public <T> Set<T> find(Class<T> target, Query<T> query) {
        return find(metadataOf(target), query);
    }

    @Override
    public <T> Set<T> all(Class<T> target) {
        return all(metadataOf(target));
    }
}
