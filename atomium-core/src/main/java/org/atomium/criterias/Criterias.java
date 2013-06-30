package org.atomium.criterias;

/**
 * @author Blackrush
 */
public final class Criterias {
    private Criterias() {}

    public static CriteriaInterface not(CriteriaInterface criteria) {
        return BoolCriteria.create(criteria, BoolCriteria.Type.NOT);
    }

    public static CriteriaInterface and(CriteriaInterface first, CriteriaInterface second) {
        return BoolBinCriteria.create(first, second, BoolBinCriteria.Type.AND);
    }

    public static CriteriaInterface or(CriteriaInterface first, CriteriaInterface second) {
        return BoolBinCriteria.create(first, second, BoolBinCriteria.Type.OR);
    }

    public static CriteriaInterface xor(CriteriaInterface first, CriteriaInterface second) {
        return BoolBinCriteria.create(first, second, BoolBinCriteria.Type.XOR);
    }

    public static CriteriaInterface equal(CriteriaInterface first, CriteriaInterface second) {
        return ComparisonCriteria.create(first, second, ComparisonCriteria.Type.EQUALS);
    }

    public static CriteriaInterface higher(CriteriaInterface first, CriteriaInterface second) {
        return ComparisonCriteria.create(first, second, ComparisonCriteria.Type.HIGHER);
    }

    public static CriteriaInterface higherOrEquals(CriteriaInterface first, CriteriaInterface second) {
        return ComparisonCriteria.create(first, second, ComparisonCriteria.Type.HIGHER_OR_EQUALS);
    }

    public static CriteriaInterface lower(CriteriaInterface first, CriteriaInterface second) {
        return ComparisonCriteria.create(first, second, ComparisonCriteria.Type.LOWER);
    }

    public static CriteriaInterface lowerOrEquals(CriteriaInterface first, CriteriaInterface second) {
        return ComparisonCriteria.create(first, second, ComparisonCriteria.Type.LOWER_OR_EQUALS);
    }

    public static CriteriaInterface like(CriteriaInterface first, CriteriaInterface second) {
        return ComparisonCriteria.create(first, second, ComparisonCriteria.Type.LIKE);
    }

    public static CriteriaInterface placeholder(String name) {
        return PlaceholderCriteria.create(name);
    }

    public static CriteriaInterface identifier(String name) {
        return IdentifierCriteria.create(name);
    }

    public static CriteriaInterface value(Object value) {
        return ValueCriteria.create(value);
    }
}
