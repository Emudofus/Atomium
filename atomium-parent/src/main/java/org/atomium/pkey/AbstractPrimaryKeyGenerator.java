package org.atomium.pkey;

/**
 * @author blackrush
 */
public abstract class AbstractPrimaryKeyGenerator<T extends PrimaryKey> implements PrimaryKeyGenerator<T> {
    @Override
    public T get() {
        return next();
    }
}
