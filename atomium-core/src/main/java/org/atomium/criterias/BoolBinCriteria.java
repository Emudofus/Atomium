package org.atomium.criterias;

/**
 * @author Blackrush
 */
public final class BoolBinCriteria extends BinaryCriteria {
    public static BoolBinCriteria create(CriteriaInterface firstEnclosingCriteria, CriteriaInterface secondEnclosingCriteria, Type type) {
        return new BoolBinCriteria(firstEnclosingCriteria, secondEnclosingCriteria, type);
    }

    public static enum Type {
        AND,
        OR,
        XOR,
    }

    private final Type type;

    private BoolBinCriteria(CriteriaInterface firstEnclosingCriteria, CriteriaInterface secondEnclosingCriteria, Type type) {
        super(firstEnclosingCriteria, secondEnclosingCriteria);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
