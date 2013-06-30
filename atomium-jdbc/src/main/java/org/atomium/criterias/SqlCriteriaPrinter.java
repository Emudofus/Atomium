package org.atomium.criterias;

/**
 * @author Blackrush
 */
public class SqlCriteriaPrinter extends CriteriaPrinter {
    protected boolean useTicks() {
        return true;
    }

    protected String escape(String identifier) {
        if (useTicks()) {
            return String.format("`%s`", identifier);
        } else {
            return identifier;
        }
    }

    protected void print(CriteriaInterface criteria, StringBuilder builder) {
        if (criteria instanceof LiteralCriteria) {
            printLiteral((LiteralCriteria) criteria, builder);
        } else if (criteria instanceof BoolCriteria) {
            printBool((BoolCriteria) criteria, builder);
        } else if (criteria instanceof BoolBinCriteria) {
            printBoolBin((BoolBinCriteria) criteria, builder);
        } else if (criteria instanceof ComparisonCriteria) {
            printComparison((ComparisonCriteria) criteria, builder);
        }
    }

    protected void printLiteral(LiteralCriteria criteria, StringBuilder builder) {
        if (criteria instanceof PlaceholderCriteria) {
            builder.append(':').append(criteria.getValue());
        } else if (criteria instanceof ValueCriteria) {
            printValue((ValueCriteria) criteria, builder);
        } else if (criteria instanceof IdentifierCriteria) {
            builder.append(escape((String) criteria.getValue()));
        }
    }

    protected void printValue(ValueCriteria criteria, StringBuilder builder) {
        Object value = criteria.getValue();

        if (value instanceof String) {
            builder.append('\'').append(criteria.getValue()).append('\'');
        } else if (value instanceof Number) {
            builder.append(value);
        }
    }

    protected void printComparison(ComparisonCriteria criteria, StringBuilder builder) {
        CriteriaInterface left = criteria.getLeft(),
                          right = criteria.getRight();
        switch (criteria.getType()) {
            case EQUALS:
                print(left, builder);
                builder.append("=");
                print(right, builder);
                break;

            case HIGHER_OR_EQUALS:
                print(left, builder);
                builder.append(" >= ");
                print(right, builder);
                break;

            case HIGHER:
                print(left, builder);
                builder.append(" > ");
                print(right, builder);
                break;

            case LOWER_OR_EQUALS:
                print(left, builder);
                builder.append(" <= ");
                print(right, builder);
                break;

            case LOWER:
                print(left, builder);
                builder.append(" < ");
                print(right, builder);
                break;

            case LIKE:
                print(left, builder);
                builder.append(" LIKE ");
                print(right, builder);
                break;
        }
    }

    protected void printBoolBin(BoolBinCriteria criteria, StringBuilder builder) {
        CriteriaInterface left = criteria.getLeft(),
                          right = criteria.getRight();
        switch (criteria.getType()) {
            case AND:
                print(left, builder);
                builder.append(" AND ");
                print(right, builder);
                break;

            case OR:
                print(left, builder);
                builder.append(" OR ");
                print(right, builder);
                break;

            case XOR:
                print(left, builder);
                builder.append(" XOR ");
                print(right, builder);
                break;
        }
    }

    void printBool(BoolCriteria criteria, StringBuilder builder) {
        CriteriaInterface encloding = criteria.getEnclosingCriteria();
        switch (criteria.getType()) {
            case NOT:
                builder.append('!');
                print(encloding, builder);
                break;
        }
    }

    @Override
    public String print(CriteriaInterface criteria) {
        StringBuilder builder = new StringBuilder();
        print(criteria, builder);
        return builder.toString();
    }
}
