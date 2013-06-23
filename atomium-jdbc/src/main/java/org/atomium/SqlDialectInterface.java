package org.atomium;

/**
 * @author Blackrush
 */
public interface SqlDialectInterface extends DialectInterface {
    /**
     * {@inheritDoc}
     */
    <T> SqlQuery buildStructure(Metadata<T> meta);

    /**
     * {@inheritDoc}
     */
    <T> SqlQuery destroyStructure(Metadata<T> meta);

    /**
     * {@inheritDoc}
     */
    <T> SqlQuery create(Metadata<T> meta, T instance);

    /**
     * {@inheritDoc}
     */
    <T> SqlQuery read(Metadata<T> meta);

    /**
     * {@inheritDoc}
     */
    <T> SqlQuery read(Ref<T> ref);

    /**
     * {@inheritDoc}
     */
    <T> SqlQuery update(Metadata<T> meta, T instance);

    /**
     * {@inheritDoc}
     */
    <T> SqlQuery delete(Metadata<T> meta, T instance);

    /**
     * {@inheritDoc}
     */
    <T> SqlQuery delete(Ref<T> ref);
}