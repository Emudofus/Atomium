package org.atomium;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

import java.sql.*;
import java.util.*;

import static com.google.common.base.Throwables.propagate;

/**
 * http://www.java2s.com/Article/Java/Database-JDBC/Named_Parameters_for_PreparedStatement.htm
 *
 * @author adam_crume
 * @author ibisek (mostly Java 1.5 modifications)
 * @author Blackrush (suppress checked exception, boxing improves)
 */
@SuppressWarnings("UnusedDeclaration")
public final class NamedParameterStatement {
    /** The statement this object is wrapping. */
    private final PreparedStatement statement;

    /** Maps parameter names to arrays of ints which are the parameter indices. */
    private final Map<String, List<Integer>> indexMap;

    /**
     * Creates a NamedParameterStatement. Wraps a call to c.
     * {@link Connection#prepareStatement(java.lang.String) prepareStatement}.
     *
     * @param connection the database connection
     * @param query the parameterized query
     */
    public NamedParameterStatement(Connection connection, String query) {
        indexMap = new HashMap<>();
        try {
            String parsedQuery = parse(query, indexMap);
            statement = connection.prepareStatement(parsedQuery, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Parses a query with named parameters. The parameter-index mappings are put into the
     * map, and the parsed query is returned. DO NOT CALL FROM CLIENT CODE. This method is
     * non-private so JUnit code can test it.
     *
     * @param query query to parse
     * @param paramMap map to hold parameter-index mappings
     * @return the parsed query
     */
    @SuppressWarnings("unchecked")
    static String parse(String query, Map<String, List<Integer>> paramMap) {
        // I was originally using regular expressions, but they didn't work well
        // for ignoring parameter-like strings inside quotes.
        int length = query.length();
        StringBuilder parsedQuery = new StringBuilder(length);
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        int index = 1;

        for (int i = 0; i < length; i++) {
            char c = query.charAt(i);
            if (inSingleQuote) {
                if (c == '\'') {
                    inSingleQuote = false;
                }
            } else if (inDoubleQuote) {
                if (c == '"') {
                    inDoubleQuote = false;
                }
            } else {
                if (c == '\'') {
                    inSingleQuote = true;
                } else if (c == '"') {
                    inDoubleQuote = true;
                } else if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(query.charAt(i + 1))) {
                    int j = i + 2;
                    while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
                        j++;
                    }
                    String name = query.substring(i + 1, j);
                    c = '?'; // replace the parameter with a question mark
                    i += name.length(); // skip past the end if the parameter

                    List<Integer> indexList = paramMap.get(name);
                    if (indexList == null) {
                        indexList = new LinkedList<>();
                        paramMap.put(name, indexList);
                    }
                    indexList.add(index);

                    index++;
                }
            }
            parsedQuery.append(c);
        }

        // make the lists of Integer immutable by Blackrush
        for (Map.Entry<String, List<Integer>> entry : paramMap.entrySet()) {
            ImmutableList<Integer> immutable = ImmutableList.copyOf(entry.getValue());
            entry.setValue(immutable);
        }

        return parsedQuery.toString();
    }

    /**
     * Returns the indexes for a parameter.
     *
     * @param name parameter name
     * @return parameter indexes
     * @throws IllegalArgumentException if the parameter does not exist
     */
    private int[] getIndexes(String name) {
        List<Integer> list = indexMap.get(name);
        if (list == null) {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }

        return Ints.toArray(list);
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(String name, Object value) {
        try {
            for (int index : getIndexes(name)) {
                statement.setObject(index, value);
            }
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(String name, String value) {
        try {
            for (int index : getIndexes(name)) {
                statement.setString(index, value);
            }
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     */
    public void setInt(String name, int value) {
        try {
            for (int index : getIndexes(name)) {
                statement.setInt(index, value);
            }
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     */
    public void setLong(String name, long value) {
        try {
            for (int index : getIndexes(name)) {
                statement.setLong(index, value);
            }
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(String name, Timestamp value) {
        try {
            for (int index : getIndexes(name)) {
                statement.setTimestamp(index, value);
            }
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     * @author ibisek
     */
    public void setBoolean(String name, boolean value) {
        try {
            for (int index : getIndexes(name)) {
                statement.setBoolean(index, value);
            }
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     * @author ibisek
     */
    public void setBytes(String name, byte[] value) {
        try {
            for (int index : getIndexes(name)) {
                statement.setBytes(index, value);
            }
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Returns the underlying statement.
     *
     * @return the statement
     */
    public PreparedStatement getStatement() {
        return statement;
    }

    /**
     * Executes the statement.
     *
     * @return true if the first result is a {@link ResultSet}
     * @see PreparedStatement#execute()
     */
    public boolean execute() {
        try {
            return statement.execute();
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Executes the statement, which must be a query.
     *
     * @return the query results
     * @see PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() {
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE statement; or
     * an SQL statement that returns nothing, such as a DDL statement.
     *
     * @return number of rows affected
     * @see PreparedStatement#executeUpdate()
     */
    public int executeUpdate() {
        try {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Closes the statement.
     *
     * @see Statement#close()
     */
    public void close() {
        try {
            statement.close();
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Adds the current set of parameters as a batch entry.
     */
    public void addBatch() {
        try {
            statement.addBatch();
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    /**
     * Executes all of the batched statements.
     *
     * See {@link Statement#executeBatch()} for details.
     *
     * @return update counts for each statement
     */
    public int[] executeBatch() {
        try {
            return statement.executeBatch();
        } catch (SQLException e) {
            throw propagate(e);
        }
    }
}