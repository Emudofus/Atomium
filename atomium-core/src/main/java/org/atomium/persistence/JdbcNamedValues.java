package org.atomium.persistence;

import com.google.common.collect.Maps;
import org.atomium.NamedValues;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Throwables.propagate;

/**
 * @author blackrush
 */
public class JdbcNamedValues extends NamedValues implements Iterable<Object> {
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

    @Override
    public Map<String, Object> toMap() {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            Map<String, Object> result = Maps.newHashMap();

            for (int i = 1; i <= metaData.getColumnCount(); ++i) {
                String name = metaData.getColumnName(i);
                Object value = resultSet.getObject(i);
                result.put(name, value);
            }

            return result;
        } catch (SQLException e) {
            throw propagate(e);
        }
    }

    @Override
    public Iterator<Object> iterator() {
        return new TheIterator();
    }

    private class TheIterator implements Iterator<Object> {
        /**
         * the first column is 1, the second is 2, ...
         */
        private int index;

        private final int columnCount;

        private TheIterator() {
            try {
                columnCount = resultSet.getMetaData().getColumnCount();
            } catch (SQLException e) {
                throw propagate(e);
            }
        }

        @Override
        public boolean hasNext() {
            return index <= columnCount;
        }

        @Override
        public Object next() {
            checkState(hasNext(), "there is no more result");
            try {
                return resultSet.getObject(++index);
            } catch (SQLException e) {
                throw propagate(e);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
