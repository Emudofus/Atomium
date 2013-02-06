package org.atomium.converter;

import org.atomium.DatabaseContext;
import org.atomium.Entity;
import org.atomium.NamedValues;
import org.atomium.TypeConverter;
import org.atomium.entity.EntityProperty;
import org.joda.time.Instant;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author blackrush
 */
public final class JodaInstantConverter implements TypeConverter {
    private JodaInstantConverter() {}
    public static final JodaInstantConverter INSTANCE = new JodaInstantConverter();

    @Override
    public <T extends Entity> NamedValues export(DatabaseContext ctx, T entity, EntityProperty<T> property) {
        Object o = property.get(entity);

        Date export = o instanceof Instant
                ? ((Instant) o).toDate()
                : new Timestamp(0);

        return NamedValues.simple().set(property.getName(), export);
    }

    @Override
    public <T extends Entity> Object extract(DatabaseContext ctx, T entity, EntityProperty<T> property, NamedValues raw) {
        Object o = raw.get(property.getName());
        return new Instant(o != null ? o : 0);
    }
}
