package org.atomium.dialects;

import org.atomium.JdbcDatabaseMetadata;

/**
 * @author Blackrush
 */
public final class HsqldbDialect extends DefaultSqlDialect {
    public HsqldbDialect(JdbcDatabaseMetadata meta) {
        super(meta);
    }

    @Override
    protected boolean useTicks() {
        return false;
    }
}
