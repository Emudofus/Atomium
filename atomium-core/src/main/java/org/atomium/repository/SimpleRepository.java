package org.atomium.repository;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.resultset.ResultSet;
import org.atomium.DatabaseContext;
import org.atomium.Entity;
import org.atomium.Repository;
import org.atomium.entity.EntityMetadata;
import org.atomium.entity.EntityProperty;
import org.atomium.pkey.PrimaryKey;

/**
 * @author blackrush
 */
public class SimpleRepository<T extends Entity> implements Repository<T> {
    private final EntityMetadata<T> metadata;
    private final IndexedCollection<T> entities = CQEngine.newInstance();

    private DatabaseContext context;

    public SimpleRepository(EntityMetadata<T> metadata) {
        this.metadata = metadata;
    }

    public EntityMetadata<T> getEntityMetadata() {
        return metadata;
    }

    @Override
    public Class<T> getEntityClass() {
        return getEntityMetadata().getEntityClass();
    }

    public boolean isSetUp() {
        return context != null;
    }

    @Override
    public void setUp(DatabaseContext context) {
        if (isSetUp()) return;

        this.context = context;

        Iterable<T> entities = context.getPersistenceStrategy().read(getEntityMetadata());
        for (T entity : entities) {
            getEntities().add(entity);
        }
    }

    @Override
    public void tearDown() {
        getContext().getPersistenceStrategy().update(getEntities(), getEntityMetadata());
    }

    protected DatabaseContext getContext() {
        return context;
    }

    protected IndexedCollection<T> getEntities() {
        return entities;
    }

    protected void insert(T entity) {
        getEntities().add(entity);
    }

    protected ResultSet<T> retrieve(EntityProperty<T> property, Object value) {
        return getEntities().retrieve(QueryFactory.equal(property, value));
    }

    protected T retrieveUnique(EntityProperty<T> property, Object value) {
        return retrieve(property, value).uniqueResult();
    }

    @Override
    public Optional<T> find(PrimaryKey key) {
        try {
            return Optional.of(retrieveUnique(getEntityMetadata().getPrimaryKeyProperty(), key));
        } catch (Exception e) {
            return Optional.absent();
        }
    }

    @Override
    public Optional<T> find(String propertyName, Object obj) {
        Optional<EntityProperty<T>> property = getEntityMetadata().getProperty(propertyName);
        if (!property.isPresent()) {
            return Optional.absent();
        }

        try {
            return Optional.of(retrieveUnique(property.get(), obj));
        } catch (Exception e) {
            return Optional.absent();
        }
    }

    @Override
    public Optional<T> find(Predicate<T> predicate) {
        for (T entity : getEntities()) {
            if (predicate.apply(entity)) {
                return Optional.of(entity);
            }
        }

        return Optional.absent();
    }

    @Override
    public Iterable<T> filter(String propertyName, Object obj) {
        Optional<EntityProperty<T>> property = getEntityMetadata().getProperty(propertyName);
        if (!property.isPresent()) {
            return ImmutableSet.of();
        }

        return retrieve(property.get(), obj);
    }

    @Override
    public Iterable<T> filter(Predicate<T> predicate) {
        ImmutableList.Builder<T> result = ImmutableList.builder();

        for (T entity : getEntities()) {
            if (predicate.apply(entity)) {
                result.add(entity);
            }
        }

        return result.build();
    }
}
