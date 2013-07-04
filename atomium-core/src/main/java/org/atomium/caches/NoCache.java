package org.atomium.caches;

import com.google.common.collect.ImmutableSet;
import com.googlecode.cqengine.query.Query;
import org.atomium.Ref;
import org.atomium.metadata.ColumnMetadata;
import org.atomium.metadata.Metadata;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Blackrush
 */
public class NoCache<T> implements CacheInterface<T> {
    private final Metadata<T> target;

    public NoCache(Metadata<T> target) {
        this.target = checkNotNull(target, "target");
    }

    @Override
    public Metadata<T> getTarget() {
        return target;
    }

    @Override
    public void put(T instance) {
    }

    @Override
    public T unique(String columnName, Object value) {
        return null;
    }

    @Override
    public T unique(ColumnMetadata<T> column, Object value) {
        return null;
    }

    @Override
    public T unique(Ref<T> ref) {
        return null;
    }

    @Override
    public Set<T> where(Query<T> query) {
        return ImmutableSet.of();
    }
}
