package org.atomium.criterias;

/**
 * @author Blackrush
 */
public final class IdentifierCriteria extends LiteralCriteria {
    private final String value;

    private IdentifierCriteria(String value) {
        this.value = value;
    }

    public static IdentifierCriteria create(String value) {
        return new IdentifierCriteria(value);
    }

    @Override
    public String getValue() {
        return value;
    }
}
