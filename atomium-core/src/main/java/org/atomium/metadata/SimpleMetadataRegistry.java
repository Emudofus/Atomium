package org.atomium.metadata;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Blackrush
 */
public class SimpleMetadataRegistry extends MetadataRegistry {
    private final Map<Class<?>, Metadata<?>> metas = Maps.newIdentityHashMap();
    private final Map<TypeToken<?>, ConverterProvider> providersFor = Maps.newHashMap(), providersFrom = Maps.newHashMap();
    private final Set<InstantiationListener> listeners = Sets.newHashSet();

    @Override
    public void register(ConverterProvider provider) {
        for (TypeToken<?> exported : provider.getExported()) {
            if (providersFor.containsKey(exported)) continue;
            providersFor.put(exported, provider);
        }
        for (TypeToken<?> extracted : provider.getExtracted()) {
            if (providersFrom.containsKey(extracted)) continue;
            providersFrom.put(extracted, provider);
        }
    }

    @Override
    public ConverterInterface getConverterFor(TypeToken<?> extracted) {
        ConverterProvider provider = providersFor.get(extracted);
        return provider != null ?
                provider.get() :
                null;
    }

    @Override
    public ConverterInterface getConverterFrom(TypeToken<?> exported) {
        ConverterProvider provider = providersFrom.get(exported);
        return provider != null ?
                provider.get() :
                null;
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
