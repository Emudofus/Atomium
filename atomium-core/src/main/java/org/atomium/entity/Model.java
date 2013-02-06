package org.atomium.entity;

import org.atomium.MutableEntity;
import org.atomium.annotation.Column;
import org.atomium.pkey.PrimaryKey;
import org.joda.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * you must define a constructor without any parameters in your model's implementation
 * @author blackrush
 */
public class Model<PK extends PrimaryKey> implements MutableEntity<PK>, Timestampable {
    @Column("id")
    protected PK primaryKey;

    @Column("created_at")
    protected Instant createdAt;

    @Column("persisted_at")
    protected Instant persistedAt;

    @Column("deleted_at")
    protected Instant deletedAt;

    /**
     * default constructor
     * @param primaryKey entity's primary key (must not be null)
     * @param createdAt the instant when this instance has been created (must not be null)
     */
    public Model(PK primaryKey, Instant createdAt) {
        this.primaryKey = checkNotNull(primaryKey);
        this.createdAt = checkNotNull(createdAt);
    }

    public Model(PK primaryKey) {
        this(primaryKey, Instant.now());
    }

    @Override
    public PK getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public void setPrimaryKey(PK pk) {
        this.primaryKey = checkNotNull(pk);
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = checkNotNull(createdAt);
    }

    @Override
    public Instant getPersistedAt() {
        return persistedAt;
    }

    @Override
    public void setPersistedAt(Instant persistedAt) {
        this.persistedAt = persistedAt;
    }

    @Override
    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}