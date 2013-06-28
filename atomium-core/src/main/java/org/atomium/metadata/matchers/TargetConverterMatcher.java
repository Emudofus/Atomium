package org.atomium.metadata.matchers;

import com.google.common.reflect.TypeToken;
import org.atomium.metadata.ColumnMetadata;
import org.atomium.metadata.ConverterMatcher;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Blackrush
 */
public final class TargetConverterMatcher implements ConverterMatcher {
    private final Set<TypeToken<?>> targets;

    private TargetConverterMatcher(Set<TypeToken<?>> targets) {
        checkArgument(checkNotNull(targets).size() >= 0);

        this.targets = targets;
    }

    public static TargetConverterMatcher create(Set<TypeToken<?>> targets) {
        return new TargetConverterMatcher(targets);
    }

    @Override
    public <T> boolean matches(ColumnMetadata<T> column) {
        for (TypeToken<?> target : targets) {
            if (target.isAssignableFrom(column.getTarget())) {
                return true;
            }
        }
        return false;
    }
}
