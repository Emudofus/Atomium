package org.atomium;

import com.google.common.collect.Maps;

import java.util.Map;

import static java.util.Arrays.asList;

/**
 * @author blackrush
 */
public abstract class NamedValues {
    public abstract Object get(String name);
    public NamedValues set(String name, Object o) {
        return this;
    }

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
    }
}
