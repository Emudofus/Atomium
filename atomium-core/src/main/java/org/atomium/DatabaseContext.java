package org.atomium;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author blackrush
 */
public class DatabaseContext {
    private final Logger log = LoggerFactory.getLogger(DatabaseContext.class);
    private final Map<Class<?>, Repository<?>> repositories = Maps.newHashMap();

    private final PersistenceStrategy persistenceStrategy;

    public DatabaseContext(PersistenceStrategy persistenceStrategy) {
        this.persistenceStrategy = persistenceStrategy;
    }

    /**
     * @return current context's {@link PersistenceStrategy}
     */
    public PersistenceStrategy getPersistenceStrategy() {
        return persistenceStrategy;
    }

    /**
     * add repository to this DatabaseContext
     * @param repository repository to add
     * @param <R> repository's type
     * @return same repository
     */
    public <R extends Repository<?>> R register(R repository) {
        repositories.put(repository.getEntityClass(), repository);
        return repository;
    }

    /**
     * should explains itself
     * @param klass entity's class
     * @param <E> entity's type
     * @return entity's repository
     */
    @SuppressWarnings("unchecked")
    public <E extends Entity> Optional<Repository<E>> get(Class<E> klass) {
        Repository<?> repository = repositories.get(klass);

        return (Optional<Repository<E>>) (repository != null ? Optional.of(repository) : Optional.absent());
    }

    /**
     * should explains itself
     * @param klass entity's class
     * @param <E> entity's type
     * @return entity's mutable repository
     */
    @SuppressWarnings("unchecked")
    public <E extends MutableEntity> Optional<MutableRepository<E>> getMutable(Class<E> klass) {
        Repository<?> repository = repositories.get(klass);

        if (repository != null && repository instanceof MutableRepository<?>) {
            MutableRepository<E> mutable = (MutableRepository<E>) repository;
            return Optional.of(mutable);
        }

        return Optional.absent();
    }

    /**
     * set up all repositories and log a successful message
     */
    public void setUp() {
        for (Repository<?> repository : repositories.values()) {
            repository.setUp(this);
            log.debug("repository {} has been set up", repository.getEntityClass().getSimpleName());
        }

        log.info("all repositories has been set up");
    }

    /**
     * tear down all repositories and log a successful message
     */
    public void tearDown() {
        for (Repository<?> repository : repositories.values()) {
            repository.tearDown();
            log.debug("repository {} has been torn down");
        }

        log.info("all repositories has been torn down");
    }
}
