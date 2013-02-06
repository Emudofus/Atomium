package org.atomium;

import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * @author blackrush
 */
public abstract class NamedValues implements Iterable<Object> {
    public abstract Object get(String name);
    public NamedValues set(String name, Object o) {
        return this;
    }
    public abstract Map<String, Object> toMap();

    public static NamedValues simple() {
        return new SimpleNamedValues();
    }

    public static NamedValues combine(Iterable<NamedValues> namedValues) {
        return new CombinedNamedValues(namedValues);
    }

    public static NamedValues combine(NamedValues... namedValues) {
        return combine(asList(namedValues));
    }

    private static class SimpleNamedValues extends NamedValues {

        private final Map<String, Object> values = Maps.newHashMap();

        @Override
        public Object get(String name) {
            return values.get(name);
        }

        @Override
        public NamedValues set(String name, Object o) {
            values.put(name, o);
            return this;
        }

        @Override
        public Map<String, Object> toMap() {
            return values;
        }

        @Override
        public Iterator<Object> iterator() {
            return values.values().iterator();
        }
    }

    private static class CombinedNamedValues extends NamedValues {

        private final Iterable<NamedValues> namedValues;

        private CombinedNamedValues(Iterable<NamedValues> namedValues) {
            this.namedValues = namedValues;
        }

        @Override
        public Object get(String name) {
            for (NamedValues iNamedValues : namedValues) {
                Object o = iNamedValues.get(name);

                if (o != null) return o;
            }
            return null;
        }

        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> result = Maps.newHashMap();
            for (NamedValues iNamedValues : namedValues) {
                result.putAll(iNamedValues.toMap());
            }
            return result;
        }

        @Override
        public Iterator<Object> iterator() {
            return new TheIterator();
        }

        private class TheIterator implements Iterator<Object> {

            private Iterator<NamedValues> it;
            private Iterator<Object> it2;

            private TheIterator() {
                it = namedValues.iterator();
                it2 = it.hasNext() ? it.next().iterator() : null;
            }

            @Override
            public boolean hasNext() {
                if (it2 == null) return false;

                if (!it2.hasNext()) {
                    if (!it.hasNext()) return false;
                    it2 = it.next().iterator();
                }

                return it2.hasNext() || hasNext();

            }

            @Override
            public Object next() {
                return it2.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
