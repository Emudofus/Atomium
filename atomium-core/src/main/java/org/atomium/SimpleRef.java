package org.atomium;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Blackrush
 */
public class SimpleRef<T> extends Ref<T> {
    private final ColumnMetadata<T> column;
    private final Object identifier;

    /**
     * create a {@link SimpleRef} instance
     * @param column the column which this ref is bound to
     * @param identifier the identifier that determines entity's identity (nullable)
     */
    public SimpleRef(ColumnMetadata<T> column, Object identifier) {
        this.column = checkNotNull(column);
        this.identifier = identifier;
    }

    @Override
    public ColumnMetadata<T> getColumn() {
        return column;
    }

    @Override
    public Object getIdentifier() {
        return identifier;
    }
}
