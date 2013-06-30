package org.atomium.criterias;

/**
 * @author Blackrush
 */
public final class PlaceholderCriteria extends LiteralCriteria {
    public static PlaceholderCriteria create(String name) {
        return new PlaceholderCriteria(name);
    }

    private final String value;

    private PlaceholderCriteria(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
