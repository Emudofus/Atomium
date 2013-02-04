package org.atomium.persistence.dialect;

import org.atomium.persistence.Dialect;

/**
 * @author blackrush
 */
public final class Dialects {
    private Dialects() {}

    public static Dialect mysql() {
        return new MySqlDialect();
    }
}
