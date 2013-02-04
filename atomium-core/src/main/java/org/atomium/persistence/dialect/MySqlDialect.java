package org.atomium.persistence.dialect;

import org.atomium.Entity;
import org.atomium.entity.EntityMetadata;
import org.atomium.persistence.Dialect;

/**
 * @author blackrush
 */
public class MySqlDialect implements Dialect {
    @Override
    public <T extends Entity> String insert(Iterable<T> entity, EntityMetadata<T> metadata) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends Entity> String update(Iterable<T> entity, EntityMetadata<T> metadata) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends Entity> String delete(Iterable<T> entity, EntityMetadata<T> metadata) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends Entity> String select(EntityMetadata<T> metadata) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
