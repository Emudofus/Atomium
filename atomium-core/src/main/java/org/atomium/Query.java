package org.atomium;

import java.util.Collection;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * @author blackrush
 */
public class Query {

    public static class Confidential {
        private final Object ref;

        private Confidential(Object ref) {
            this.ref = ref;
        }

        public Object get() {
            return ref;
        }
    }

    public static Query create(String command, Collection<Object> parameters) {
        return new Query(command, parameters);
    }

    public static Query create(String command, Object... parameters) {
        return new Query(command, asList(parameters));
    }

    public static Query create(String command) {
        return new Query(command, Collections.emptyList());
    }

    public static Object confidential(Object ref) {
        return new Confidential(ref);
    }

    private final String command;
    private final Collection<Object> parameters;

    protected Query(String command, Collection<Object> parameters) {
        this.command = checkNotNull(command);
        this.parameters = checkNotNull(parameters);
    }

    public String getCommand() {
        return command;
    }

    public Collection<Object> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        boolean first;
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(command).append(" ");

        stringBuilder.append('[');
        first = true;
        for (Object parameter : parameters) {
            if (first) first = false;
            else stringBuilder.append(',');

            stringBuilder.append(parameter);
        }
        stringBuilder.append(']');

        return stringBuilder.toString();
    }
}
