package org.atomium.caches;

import com.googlecode.cqengine.query.Query;
import org.atomium.Ref;
import org.atomium.metadata.ColumnMetadata;
import org.atomium.metadata.Metadata;

import java.util.Collection;
import java.util.Set;

/**
 * @author Blackrush
 */
public interface CacheInterface<T> {
    Metadata<T> getTarget();

    void put(T instance);
    void put(Collection<T> instances);

    T unique(String columnName, Object value);
    T unique(ColumnMetadata<T> column, Object value);
    T unique(Ref<T> ref);

    Set<T> where(Query<T> query);

    Set<T> all();
}
