package org.atomium;

import org.atomium.metadata.ColumnMetadata;
import org.atomium.metadata.Metadata;

/**
 * @author Blackrush
 */
public abstract class Ref<T> {

    /**
     * the column which this ref is bound to
     * @return the non-null column
     */
    public abstract ColumnMetadata<T> getColumn();

    /**
     * the identifier that determines entity's identity
     * @return the identifier or null
     */
    public abstract Object getIdentifier();

    /**
     * get the entity's metadata that this ref represents
     * @return the non-null entity's metadata
     */
    public Metadata<T> getEntityMetadata() {
        return getColumn().getParent();
    }
}
