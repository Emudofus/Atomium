package org.atomium.converter;

import org.atomium.DatabaseContext;
import org.atomium.Entity;
import org.atomium.TypeConverter;
import org.atomium.entity.EntityProperty;
import org.joda.time.Instant;

import java.sql.Timestamp;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;

/**
 * @author blackrush
 */
public final class JodaInstantConverter implements TypeConverter {
    private JodaInstantConverter() {}
    public static final JodaInstantConverter INSTANCE = new JodaInstantConverter();

    @Override
    public <T extends Entity> Map<String, Object> export(DatabaseContext ctx, T entity, EntityProperty<T> property) {
        Object o = property.get(entity);

        Timestamp export = new Timestamp(o instanceof Instant ? ((Instant) o).getMillis() : 0);

        return of(property.getName(), (Object) export);
    }

    @Override
    public <T extends Entity> Object extract(DatabaseContext ctx, T entity, EntityProperty<T> property, Map<String, Object> raw) {
        Object o = raw.get(property.getName());
        return new Instant(o != null ? o : 0);
    }
}
