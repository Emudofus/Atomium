package org.atomium.dialects.printers;

import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.LogicalQuery;
import com.googlecode.cqengine.query.logical.Not;
import com.googlecode.cqengine.query.logical.Or;
import com.googlecode.cqengine.query.simple.*;
import org.atomium.QueryPrinter;

/**
 * @author Blackrush
 */
public class DefaultQueryPrinter<T> extends QueryPrinter<T> {
    protected String escapeAttribute(String attribute) {
        return "`" + attribute + "`";
    }

    protected String escapeValue(Object value) {
        if (value instanceof Number) {
            return value.toString();
        }
        return "'" + value + "'";
    }

    @Override
    protected void visitAnd(And<T> query) {
        boolean first = true;
        for (LogicalQuery<T> child : query.getLogicalQueries()) {
            if (first) first = false;
            else builder.append(" AND ");
            visit(child);
        }
        for (SimpleQuery<T, ?> child : query.getSimpleQueries()) {
            if (first) first = false;
            else builder.append(" AND ");
            visit(child);
        }
    }

    @Override
    protected void visitOr(Or<T> query) {
        boolean first = true;
        for (LogicalQuery<T> child : query.getLogicalQueries()) {
            if (first) first = false;
            else builder.append(" OR ");
            visit(child);
        }
        for (SimpleQuery<T, ?> child : query.getSimpleQueries()) {
            builder.append(" OR ");
            visit(child);
        }
    }

    @Override
    protected void visitNot(Not<T> query) {
        builder.append("NOT ");
        visit(query.getNegatedQuery());
    }

    @Override
    protected void visitEqual(Equal<T, ?> query) {
        builder
            .append(escapeAttribute(query.getAttributeName()))
            .append('=')
            .append('\'').append(query.getValue()).append('\'')
        ;
    }

    @Override
    protected void visitGreaterThan(GreaterThan<T, ?> query) {
        builder
                .append(escapeAttribute(query.getAttributeName()))
                .append(query.isValueInclusive() ? " >= " : " > ")
                .append(escapeValue(query.getValue()))
        ;
    }

    @Override
    protected void visitLessThan(LessThan<T, ?> query) {
        builder
                .append(escapeAttribute(query.getAttributeName()))
                .append(query.isValueInclusive() ? " <= " : " < ")
                .append(escapeValue(query.getValue()))
        ;
    }

    @Override
    protected void visitBetween(Between<T, ?> query) {
        builder
            .append(escapeAttribute(query.getAttributeName()))
            .append(" BETWEEN ")
            .append(query.getLowerValue())
            .append(" AND ")
            .append(query.getUpperValue())
        ;
    }

    @Override
    protected void visitHas(Has<T, ?> query) {
        builder
            .append(escapeAttribute(query.getAttributeName()))
            .append(" IS NOT NULL")
        ;
    }

    @Override
    protected void visitStringContains(StringContains<T, ?> query) {
        builder
            .append(escapeAttribute(query.getAttributeName()))
            .append(" LIKE ")
            .append(escapeValue("%" + query.getValue() + "%"))
        ;
    }

    @Override
    protected void visitStringEndsWith(StringEndsWith<T, ?> query) {
        builder
                .append(escapeAttribute(query.getAttributeName()))
                .append(" LIKE ")
                .append(escapeValue("%" + query.getValue()))
        ;
    }

    @Override
    protected void visitStringStartsWith(StringStartsWith<T, ?> query) {
        builder
                .append(escapeAttribute(query.getAttributeName()))
                .append(" LIKE ")
                .append(escapeValue(query.getValue() + "%"))
        ;
    }

    @Override
    protected void visitStringIsContainedIn(StringIsContainedIn<T, ?> query) {
        // TODO print CI query
    }
}
