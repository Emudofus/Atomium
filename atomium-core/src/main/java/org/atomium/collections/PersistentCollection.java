package org.atomium.collections;

import org.atomium.Ref;

import java.util.Collection;

/**
 * @author Blackrush
 */
public interface PersistentCollection<T, O> extends Collection<T> {
    /**
     * get the owner of this collection
     * this is a method that lazily retrieves owner's instance according to its reference
     * TL;DR this is just a convenient shortcut method for :
     * <code>
     * Database db = ...;
     * PersistentCollection<T, O> collection = ...;
     * O owner = db.find(collection.getOwnerRef());
     * </code>
     * @return the non-null owner
     * @throws IllegalStateException if the owner doesn't exist anymore
     */
    O getOwner();

    /**
     * get the owner's reference of this collection
     * @return the non-null owner's reference
     */
    Ref<O> getOwnerRef();

    /**
     * this method will creates all the relationship with this collection's owner then persist the instance into
     * the database and finally add the instance to the collection
     * @param element the non-null instance
     */
    void persist(T element);

    /**
     * this method will destroy any relationship with this collection's owner then remove the instance from the database
     * and finally remove the instance from the collection
     * @param element the non-null instance
     */
    void destroy(T element);
}
