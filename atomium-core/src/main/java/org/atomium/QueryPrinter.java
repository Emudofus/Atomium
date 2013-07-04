package org.atomium;

import com.googlecode.cqengine.query.Query;
import org.atomium.QueryVisitor;

/**
 * @author Blackrush
 */
public abstract class QueryPrinter<T> extends QueryVisitor<T> {
    protected StringBuilder builder;

    public final String print(Query<T> query) {
        builder = new StringBuilder();
        visit(query);
        return builder.toString();
    }
}
