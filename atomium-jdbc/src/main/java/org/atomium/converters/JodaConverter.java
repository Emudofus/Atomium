package org.atomium.converters;

import com.google.common.collect.ImmutableSet;
import org.atomium.NamedValues;
import org.atomium.metadata.*;
import org.joda.time.DateTime;
import org.joda.time.Instant;

import java.sql.Date;
import java.sql.Timestamp;

import static com.google.common.reflect.TypeToken.of;

/**
 * @author Blackrush
 */
public final class JodaConverter implements ConverterInterface {
    @Override
    public void setMetadataRegistry(MetadataRegistry registry) {
        // ignore
    }

    @Override
    public ConverterMatcher getMatcher() {
        return ConverterMatchers.withTarget(of(DateTime.class), of(Instant.class));
    }

    @Override
    public <T> ImmutableSet<ColumnInfo> getBuiltStructure(ColumnMetadata<T> column) {
        return ImmutableSet.of(column.asColumnInfo());
    }

    @Override
    public <T> boolean extract(ColumnMetadata <T> column, T instance, NamedValues input) {
        Object o = input.get(column.getName());

        if (o instanceof Date) {
            column.set(instance, new DateTime(((Date) o).getTime()));
            return true;
        } else if (o instanceof Timestamp) {
            column.set(instance, new Instant(((Timestamp) o).getTime()));
            return true;
        }

        return false;
    }

    @Override
    public <T> boolean export(ColumnMetadata<T> column, T instance, NamedValues output) {
        Object o = column.get(instance);

        if (o instanceof DateTime) {
            output.set(column.getName(), new Date(((DateTime) o).getMillis()));
            return true;
        } else if (o instanceof Instant) {
            output.set(column.getName(), new Timestamp(((Instant) o).getMillis()));
            return true;
        }

        return false;
    }
}
