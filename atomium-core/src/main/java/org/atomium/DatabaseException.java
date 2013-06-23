package org.atomium;

/**
 * @author Blackrush
 */
public abstract class DatabaseException extends RuntimeException {
    private DatabaseException(String message) {
        super(message);
    }

    private DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class NotFound extends DatabaseException {
        public NotFound(String message) {
            super(message);
        }

        public NotFound(String message, Throwable cause) {
            super(message, cause);
        }

        public NotFound(Ref<?> ref) {
            this(String.format(
                    "can't find any %s with %s=%s",
                    ref.getEntityMetadata().getTableName(),
                    ref.getColumn().getName(),
                    ref.getIdentifier()
            ));
        }
    }

    public static class NonUnique extends DatabaseException {
        public NonUnique(String message) {
            super(message);
        }

        public NonUnique(String message, Throwable cause) {
            super(message, cause);
        }

        public NonUnique() {
            this("you wanted a single values but found more than one");
        }
    }
}
