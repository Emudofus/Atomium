package org.atomium.criterias;

/**
 * @author Blackrush
 */
public final class BoolCriteria extends UnaryCriteria {
    public static BoolCriteria create(CriteriaInterface enclosingCriteria, Type type) {
        return new BoolCriteria(enclosingCriteria, type);
    }

    public static enum Type {
        NOT,
    }

    private final Type type;

    private BoolCriteria(CriteriaInterface enclosingCriteria, Type type) {
        super(enclosingCriteria);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
