package org.atomium.caches;

import com.google.common.collect.ImmutableSet;
import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import org.atomium.Ref;
import org.atomium.metadata.ColumnMetadata;
import org.atomium.metadata.Metadata;

import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.googlecode.cqengine.query.QueryFactory.equal;

/**
 * @author Blackrush
 */
public class CQCache<T> implements CacheInterface<T> {
    private final Metadata<T> target;
    private final IndexedCollection<T> list = CQEngine.newInstance();

    protected CQCache(Metadata<T> target) {
        this.target = checkNotNull(target, "target");
        this.list.addIndex(UniqueIndex.onAttribute(this.target.getPrimaryKey()));
    }

    public static <T> CQCache<T> of(Metadata<T> target) {
        return new CQCache<>(target);
    }

    @Override
    public Metadata<T> getTarget() {
        return target;
    }

    @Override
    public void put(T instance) {
        list.add(instance);
    }

    @Override
    public void put(Collection<T> instances) {
        list.addAll(instances);
    }

    @Override
    public T unique(String columnName, Object value) {
        return unique(target.getColumn(columnName), value);
    }

    @Override
    public T unique(ColumnMetadata<T> column, Object value) {
        return unique(column.getRef(value));
    }

    @Override
    public T unique(Ref<T> ref) {
        Query<T> query = equal(ref.getColumn(), ref.getIdentifier());
        ResultSet<T> result = list.retrieve(query);
        return result.uniqueResult();
    }

    @Override
    public Set<T> where(Query<T> query) {
        return ImmutableSet.copyOf(list.retrieve(query));
    }
}
