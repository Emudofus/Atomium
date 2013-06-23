package org.atomium;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * @author Blackrush
 */
public final class SqlQuery implements QueryInterface {
    private final String command;
    private final NamedValues values;

    private SqlQuery(String command, NamedValues values) {
        this.command = command;
        this.values = values;
    }

    public static SqlQuery create(String query, NamedValues values) {
        return new SqlQuery(query, values);
    }

    public static SqlQuery create(String query) {
        return create(query, NamedValues.of());
    }

    public static SqlQuery create(String query, Object... args) {
        if (args.length == 0) return create(query);
        return create(String.format(query, args));
    }

    public static SqlQuery create(NamedValues values, String query, Object... args) {
        if (args.length == 0) return create(query, values);
        return create(String.format(query, args), values);
    }

    public String getCommand() {
        return command;
    }

    @Override
    public NamedValues getBoundValues() {
        return values;
    }

    @Override
    public Object getBoundValue(String key) {
        return values.get(key);
    }

    public NamedParameterStatement statement(Connection connection) {
        NamedParameterStatement statement = new NamedParameterStatement(connection, command);

        for (NamedValues.Entry entry : values) {
            statement.setObject(entry.key, entry.value);
        }

        return statement;
    }

    public void execute(Connection connection) {
        NamedParameterStatement statement = statement(connection);
        statement.execute();
    }

    public ResultSet query(Connection connection) {
        return statement(connection).executeQuery();
    }

    @Override
    public String toString() {
        return command;
    }
}
