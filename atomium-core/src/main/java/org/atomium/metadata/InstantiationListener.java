package org.atomium.metadata;

/**
 * @author Blackrush
 */
public interface InstantiationListener {
    <T> void listen(Metadata<T> meta, T instance);
}
