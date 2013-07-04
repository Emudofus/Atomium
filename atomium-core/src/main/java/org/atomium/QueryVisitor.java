package org.atomium;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.Not;
import com.googlecode.cqengine.query.logical.Or;
import com.googlecode.cqengine.query.simple.*;

/**
 * @author Blackrush
 */
public class QueryVisitor<T> {
    public void visit(Query<T> query) {
        if (query instanceof And<?>) visitAnd((And<T>) query);
        else if (query instanceof Between<?, ?>) visitBetween((Between<T, ?>) query);
        else if (query instanceof Equal<?, ?>) visitEqual((Equal<T, ?>) query);
        else if (query instanceof GreaterThan<?, ?>) visitGreaterThan((GreaterThan<T, ?>) query);
        else if (query instanceof Has<?, ?>) visitHas((Has<T, ?>) query);
        else if (query instanceof LessThan<?, ?>) visitLessThan((LessThan<T, ?>) query);
        else if (query instanceof Not<?>) visitNot((Not<T>) query);
        else if (query instanceof Or<?>) visitOr((Or<T>) query);
        else if (query instanceof StringContains<?, ?>) visitStringContains((StringContains<T, ?>) query);
        else if (query instanceof StringEndsWith<?, ?>) visitStringEndsWith((StringEndsWith<T, ?>) query);
        else if (query instanceof StringIsContainedIn<?, ?>) visitStringIsContainedIn((StringIsContainedIn<T, ?>) query);
        else if (query instanceof StringStartsWith<?, ?>) visitStringStartsWith((StringStartsWith<T, ?>) query);
        else fallback(query);
    }

    protected void fallback(Query<T> query) {

    }

    protected void visitAnd(And<T> query) {
        fallback(query);
    }

    protected void visitBetween(Between<T, ?> query) {
        fallback(query);
    }

    protected void visitEqual(Equal<T, ?> query) {
        fallback(query);
    }

    protected void visitGreaterThan(GreaterThan<T, ?> query) {
        fallback(query);
    }

    protected void visitHas(Has<T, ?> query) {
        fallback(query);
    }

    protected void visitLessThan(LessThan<T, ?> query) {
        fallback(query);
    }

    protected void visitNot(Not<T> query) {
        fallback(query);
    }

    protected void visitOr(Or<T> query) {
        fallback(query);
    }

    protected void visitStringContains(StringContains<T, ?> query) {
        fallback(query);
    }

    protected void visitStringEndsWith(StringEndsWith<T, ?> query) {
        fallback(query);
    }

    protected void visitStringIsContainedIn(StringIsContainedIn<T, ?> query) {
        fallback(query);
    }

    protected void visitStringStartsWith(StringStartsWith<T, ?> query) {
        fallback(query);
    }
}
