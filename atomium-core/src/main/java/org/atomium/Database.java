package org.atomium;

/**
 * @author Blackrush
 */
public abstract class Database implements DatabaseInterface {
    private final MetadataRegistry registry;

    protected Database(MetadataRegistry registry) {
        this.registry = registry;
    }

    @Override
    public MetadataRegistry getRegistry() {
        return registry;
    }

    @Override
    public <T> T findOne(Class<T> target, Object identifier) {
        Metadata<T> meta = registry.get(target);
        Ref<T> ref = meta.getPrimaryKey().getRef(identifier);
        return findOne(ref);
    }

    @Override
    public <T> T findOne(Class<T> target, String column, Object value) {
        Metadata<T> meta = registry.get(target);
        ColumnMetadata<T> columnMeta = meta.getColumn(column);
        return findOne(columnMeta.getRef(value));
    }
}
