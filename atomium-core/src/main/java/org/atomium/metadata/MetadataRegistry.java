package org.atomium.metadata;

import com.google.common.reflect.TypeToken;

/**
 * @author Blackrush
 */
public abstract class MetadataRegistry {
    /**
     * register a {@link ConverterInterface} by a {@link ConverterProvider}
     * @param provider the {@link ConverterInterface} provider
     * @see {@link ConverterProvider}
     */
    public abstract void register(ConverterProvider provider);

    /**
     * get a converter that can extract values of given type in argument
     * @param extracted the type
     * @return a new converter instance or null if there are no registered converter for that type
     */
    public abstract ConverterInterface getConverterFor(TypeToken<?> extracted);

    /**
     * get a converter that can export values of given type in argument
     * @param exported the type
     * @return a new converter instance or null if there are no registered converter for that type
     */
    public abstract ConverterInterface getConverterFrom(TypeToken<?> exported);

    /**
     * register a {@link Metadata} by a {@link Class}
     * @param target the class used to build entity's metadata
     * @param <T> entity's type
     */
    public abstract <T> Metadata<T> register(Class<T> target);

    /**
     * get the {@link Metadata} by its {@link Class}
     * @param target the class representing the entity
     * @param <T> entity's type
     * @return the metadata or null if no metadata for that class is registered
     */
    public abstract <T> Metadata<T> get(Class<? extends T> target);
}
