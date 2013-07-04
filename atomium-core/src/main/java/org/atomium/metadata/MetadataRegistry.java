package org.atomium.metadata;

import java.util.Set;

/**
 * @author Blackrush
 */
public abstract class MetadataRegistry {
    /**
     * register a {@link ConverterInterface}
     * @param converter the non-null {@link ConverterInterface} instance
     * @see {@link ConverterInterface}
     */
    public abstract void register(ConverterInterface converter);

    /**
     * get a converter that can extract values of the given column in argument
     * @param column the column
     * @return the {@link ConverterInterface} instance or null if none match the given column
     */
    public abstract <T> ConverterInterface getConverter(ColumnMetadata<T> column);

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

    /**
     * get the {@link Metadata} by its {@link Class} according to {@code T}
     * @param instance the entity
     * @param <T> entity's type
     * @return the metadata or null if no metadata for that class is registered
     */
    public abstract <T> Metadata<T> get(T instance);

    /**
     * get the set of all registered {@link Metadata}
     * @return a non-null set
     */
    public abstract Set<Metadata<?>> getRegisteredMetadata();

    /**
     * add a {@link InstantiationListener} to this registry that will listen all entity instantation
     * @param listener the non-null listener
     * @return {@code true} if you successfully add a listen, {@code false} otherwise
     */
    public abstract boolean addInstantationListener(InstantiationListener listener);

    /**
     * propagate instantiation event to all registered listeners
     * @param instance the non-null entity
     * @param <T> the entity's type
     */
    public abstract <T> void onInstantiated(T instance);
}
