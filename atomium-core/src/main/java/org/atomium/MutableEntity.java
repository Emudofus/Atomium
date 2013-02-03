package org.atomium;

import org.atomium.pkey.PrimaryKey;

/**
 * @author blackrush
 */
public interface MutableEntity<PK extends PrimaryKey> extends Entity {
    @Override
    PK getPrimaryKey();

    void setPrimaryKey(PK key);
}
