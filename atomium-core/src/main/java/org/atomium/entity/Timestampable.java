package org.atomium.entity;

import org.joda.time.Instant;

/**
 * convert to mixin ?
 * @author blackrush
 */
public interface Timestampable {
    /**
     * @return the instant when this instance has been created (must not be null)
     */
    Instant getCreatedAt();

    /**
     * @param createdAt the instant when this instance has been created (must not be null)
     */
    void setCreatedAt(Instant createdAt);

    /**
     * FIXME convert it to Optional ?
     * @return the instant when this instance has been persisted
     */
    Instant getPersistedAt();

    /**
     * @param persistedAt the instant when this instance has been persisted
     */
    void setPersistedAt(Instant persistedAt);

    /**
     * FIXME convert it to Optional ?
     * @return the instant when this instance has been deleted
     */
    Instant getDeletedAt();

    /**
     * @param deletedAt the instant when this instance has been deleted
     */
    void setDeletedAt(Instant deletedAt);
}
