package org.atomium;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Throwables.propagate;

/**
 * @author Blackrush
 */
public abstract class NamedValues implements Iterable<NamedValues.Entry> {
    public static class Entry {
        public final String key;
        public final Object value;

        private Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public static Entry of(String key, Object value) {
            return new Entry(key, value);
        }
    }

    public abstract Object get(String key);
    public abstract boolean has(String key);
    public abstract NamedValues set(String key, Object value);
    public abstract int length();
    public abstract Set<String> keyView();
    public abstract Collection<Object> valueView();
    public abstract Map<String, Object> mapView();

    public static NamedValues of(ResultSet rset) {
        return new JDBC(rset);
    }

    public static NamedValues of(Map<String, Object> values) {
        return new Simple(values);
    }

    public static NamedValues of() {
        return new Simple(Maps.<String, Object>newHashMap());
    }

    static final class JDBC extends NamedValues {
        private final ResultSet rset;

        private JDBC(ResultSet rset) {
            this.rset = rset;
        }

        @Override
        public Object get(String key) {
            try {
                return rset.getObject(key);
            } catch (SQLException e) {
                // we can't know if there is a database error
                // or if the given key is invalid
                // so just return null for now
                return null;
            }
        }

        @Override
        public boolean has(String key) {
            try {
                rset.findColumn(key);
                return true;
            } catch (SQLException e) {
                return false;
            }
        }

        @Override
        public NamedValues set(String key, Object value) {
            // ignored
            return this;
        }

        @Override
        public int length() {
            try {
                return rset.getMetaData().getColumnCount();
            } catch (SQLException e) {
                throw propagate(e);
            }
        }

        @Override
        public Set<String> keyView() {
            ImmutableSet.Builder<String> view = ImmutableSet.builder();
            for (int i = 1; i <= length(); i++) {
                try {
                    view.add(rset.getMetaData().getColumnName(i));
                } catch (SQLException e) {
                    throw propagate(e);
                }
            }
            return view.build();
        }

        @Override
        public Collection<Object> valueView() {
            ImmutableList.Builder<Object> view = ImmutableList.builder();
            for (int i = 1; i <= length(); i++) {
                try {
                    view.add(rset.getObject(i));
                } catch (SQLException e) {
                    throw propagate(e);
                }
            }
            return view.build();
        }

        @Override
        public Map<String, Object> mapView() {
            Map<String, Object> view = Maps.newHashMap();
            for (Entry entry : this) {
                view.put(entry.key, entry.value);
            }
            return view;
        }

        @Override
        public Iterator<Entry> iterator() {
            return new It();
        }

        class It implements Iterator<Entry> {
            int index = 1;

            @Override
            public boolean hasNext() {
                return index <= length();
            }

            @Override
            public Entry next() {
                try {
                    return Entry.of(rset.getMetaData().getColumnName(index), rset.getObject(index));
                } catch (SQLException e) {
                    throw propagate(e);
                }
            }

            @Override
            public void remove() {
                // ignored
            }
        }
    }

    static final class Simple extends NamedValues {
        private final Map<String, Object> values;

        Simple(Map<String, Object> values) {
            this.values = values;
        }

        @Override
        public Object get(String key) {
            return values.get(key);
        }

        @Override
        public boolean has(String key) {
            return values.containsKey(key);
        }

        @Override
        public NamedValues set(String key, Object value) {
            values.put(key, value);
            return this;
        }

        @Override
        public int length() {
            return values.size();
        }

        @Override
        public Set<String> keyView() {
            return ImmutableSet.copyOf(values.keySet());
        }

        @Override
        public Collection<Object> valueView() {
            return values.values();
        }

        @Override
        public Map<String, Object> mapView() {
            return ImmutableMap.copyOf(values);
        }

        @Override
        public Iterator<Entry> iterator() {
            return new It();
        }

        class It implements Iterator<Entry> {
            Iterator<Map.Entry<String, Object>> it = values.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Entry next() {
                Map.Entry<String, Object> entry = it.next();
                return Entry.of(entry.getKey(), entry.getValue());
            }

            @Override
            public void remove() {
                // ignored
            }
        }
    }
}
