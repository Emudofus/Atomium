package org.atomium.metadata;

import com.google.common.collect.ImmutableSet;

import static com.google.common.collect.ImmutableSet.of;

/**
 * @author Blackrush
 */
public abstract class Converter implements ConverterInterface {
    private MetadataRegistry registry;

    protected MetadataRegistry getRegistry() {
        return registry;
    }

    @Override
    public void setMetadataRegistry(MetadataRegistry registry) {
        this.registry = registry;
    }

    @Override
    public <T> ImmutableSet<ColumnInfo> getBuiltStructure(ColumnMetadata<T> column) {
        return of(column.asColumnInfo());
    }
}
