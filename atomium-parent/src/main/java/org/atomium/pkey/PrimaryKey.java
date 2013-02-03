package org.atomium.pkey;

/**
 * @author blackrush
 */
public interface PrimaryKey extends Comparable<PrimaryKey> {
    // force implementation
    boolean equals(Object other);
    int hashCode();
    String toString();
}
