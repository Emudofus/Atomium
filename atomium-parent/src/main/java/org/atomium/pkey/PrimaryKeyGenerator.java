package org.atomium.pkey;

import com.google.common.base.Supplier;

/**
 * @author blackrush
 */
public interface PrimaryKeyGenerator<T extends PrimaryKey> extends Supplier<T> {
    void init(T initial);
    T next();
}
