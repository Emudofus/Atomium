package org.atomium.criterias;

/**
 * @author Blackrush
 */
public final class ValueCriteria extends LiteralCriteria {
    private final Object value;

    private ValueCriteria(Object value) {
        this.value = value;
    }

    public static ValueCriteria create(Object value) {
        return new ValueCriteria(value);
    }

    @Override
    public Object getValue() {
        return value;
    }
}
