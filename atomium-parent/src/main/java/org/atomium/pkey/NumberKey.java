package org.atomium.pkey;

/**
 * @author blackrush
 */
public abstract class NumberKey<T extends Number> implements PrimaryKey {
    public abstract Number toNumber();

    @Override
    public int compareTo(PrimaryKey primaryKey) {
        if (primaryKey != null && primaryKey instanceof NumberKey) {
            return toNumber().hashCode() - ((NumberKey) primaryKey).toNumber().hashCode();
        }

        return 0; // can't compare here (should I throw an exception?)
    }

    @Override
    public int hashCode() {
        return toNumber().hashCode();
    }

    @Override
    public String toString() {
        return toNumber().toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null &&
               obj instanceof NumberKey &&
               toNumber().equals(((NumberKey) obj).toNumber());
    }
}
