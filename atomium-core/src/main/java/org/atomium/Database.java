package org.atomium;

import com.google.common.collect.Maps;
import org.atomium.criterias.CriteriaInterface;
import org.atomium.criterias.Criterias;
import org.atomium.metadata.ColumnMetadata;
import org.atomium.metadata.Metadata;
import org.atomium.metadata.MetadataRegistry;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Blackrush
 */
public abstract class Database implements DatabaseInterface {
    private static final Map<String, DatabaseProvider> providers = Maps.newHashMap();

    public static void register(String urlRegex, DatabaseProvider provider) {
        providers.put(urlRegex, provider);
    }

    public static DatabaseProvider forUrl(String url) {
        for (Map.Entry<String, DatabaseProvider> entry : providers.entrySet()) {
            if (url.matches(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private final MetadataRegistry registry;

    protected Database(MetadataRegistry registry) {
        this.registry = registry;
    }

    protected <T> Metadata<T> metadataOf(Class<T> target) {
        Metadata<T> meta = registry.get(target);
        checkArgument(meta != null, "%s must be registered", target);
        return meta;
    }

    protected <T> Metadata<T> metadataOf(T instance) {
        Metadata<T> meta = registry.get(instance);
        checkArgument(meta != null, "%s must be registered", instance.getClass());
        return meta;
    }

    protected <T> CriteriaInterface createCriteria(ColumnMetadata<T> column, Object value) {
        CriteriaInterface left = Criterias.identifier(column.getName()),
                          right = Criterias.value(value);

        return Criterias.equal(left, right);
    }

    @Override
    public <T> Ref<T> ref(Class<T> target, Object identifier) {
        return metadataOf(target).getPrimaryKey().getRef(identifier);
    }

    @Override
    public <T> Ref<T> ref(Class<T> target, String column, Object identifier) {
        ColumnMetadata<T> column0 = metadataOf(target).getColumn(column);
        checkNotNull(column0, "can't find any column %s", column);

        return column0.getRef(identifier);
    }

    @Override
    public MetadataRegistry getRegistry() {
        return registry;
    }

    @Override
    public <T> T findOne(Class<T> target, Object identifier) {
        return findOne(ref(target, identifier));
    }

    @Override
    public <T> T findOne(Class<T> target, String column, Object value) {
        return findOne(ref(target, column, value));
    }

    @Override
    public <T> Set<T> find(Class<T> target, String columnName, Object columnValue) {
        ColumnMetadata<T> column = metadataOf(target).getColumn(columnName);
        checkArgument(column != null, "unknown column %s on %s", columnName, target.getName());

        CriteriaInterface criteria = createCriteria(column, columnValue);

        return find(target, criteria);
    }
}
