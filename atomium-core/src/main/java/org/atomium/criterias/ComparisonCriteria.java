package org.atomium.criterias;

/**
 * @author Blackrush
 */
public final class ComparisonCriteria extends BinaryCriteria {
    public static ComparisonCriteria create(CriteriaInterface firstEnclosingCriteria, CriteriaInterface secondEnclosingCriteria, Type type) {
        return new ComparisonCriteria(firstEnclosingCriteria, secondEnclosingCriteria, type);
    }

    public static enum Type {
        EQUALS,
        HIGHER,
        LOWER,
        HIGHER_OR_EQUALS,
        LOWER_OR_EQUALS,
        LIKE,
    }

    private final Type type;

    private ComparisonCriteria(CriteriaInterface firstEnclosingCriteria, CriteriaInterface secondEnclosingCriteria, Type type) {
        super(firstEnclosingCriteria, secondEnclosingCriteria);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
