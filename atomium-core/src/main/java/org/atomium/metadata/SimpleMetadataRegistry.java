package org.atomium.metadata;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Blackrush
 */
public class SimpleMetadataRegistry extends MetadataRegistry {
    private final Map<Class<?>, Metadata<?>> metas = Maps.newIdentityHashMap();
    private final Set<ConverterInterface> converters = Sets.newHashSet();
    private final Set<InstantiationListener> listeners = Sets.newHashSet();

    @Override
    public void register(ConverterInterface converter) {
        converters.add(checkNotNull(converter));
        converter.setMetadataRegistry(this);
    }

    @Override
    public <T> ConverterInterface getConverter(ColumnMetadata<T> column) {
        for (ConverterInterface converter : converters) {
            if (converter.getMatcher().matches(column)) {
                return converter;
            }
        }
        return null;
    }

    @Override
    public <T> Metadata<T> register(Class<T> target) {
        if (metas.containsKey(target)) return null;

        Metadata<T> meta = new Metadata<>(this, target);
        metas.put(target, meta);

        meta.load();
        return meta;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Metadata<T> get(Class<? extends T> target) {
        return (Metadata<T>) metas.get(target);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Metadata<T> get(T instance) {
        return (Metadata<T>) metas.get(instance.getClass());
    }

    @Override
    public boolean addInstantationListener(InstantiationListener listener) {
        return listeners.add(checkNotNull(listener));
    }

    @Override
    public <T> void onInstantiated(T instance) {
        Metadata<T> meta = get(checkNotNull(instance));
        checkArgument(meta != null, "unknown entity %s", instance.getClass());

        for (InstantiationListener listener : listeners) {
            listener.listen(meta, instance);
        }
    }
}
