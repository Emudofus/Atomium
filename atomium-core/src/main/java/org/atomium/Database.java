package org.atomium;

import com.google.common.collect.Maps;
import org.atomium.metadata.ColumnMetadata;
import org.atomium.metadata.Metadata;
import org.atomium.metadata.MetadataRegistry;

import java.util.Map;

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
}
