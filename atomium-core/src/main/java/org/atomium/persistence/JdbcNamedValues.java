package org.atomium.persistence;

import org.atomium.NamedValues;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;

/**
 * @author blackrush
 */
public class JdbcNamedValues extends NamedValues {
    public static JdbcNamedValues of(ResultSet rset) {
        return new JdbcNamedValues(rset);
    }

    private final ResultSet resultSet;

    private JdbcNamedValues(ResultSet resultSet) {
        this.resultSet = checkNotNull(resultSet);
    }

    @Override
    public Object get(String name) {
        try {
            return resultSet.getObject(name);
        } catch (SQLException e) {
            throw propagate(e);
        }
    }
}
