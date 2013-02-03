package org.atomium.pkey;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * @author blackrush
 */
public final class PrimaryKeyGenerators {
    private PrimaryKeyGenerators() {}

    private static final Map<Class<?>, Class<?>> keyToGenerator =
            ImmutableMap.<Class<?>, Class<?>>builder()
                .put(IntKey.class, IntKeyGenerator.class)
                .put(LongKey.class, LongKeyGenerator.class)
            .build();

    public static Optional<PrimaryKeyGenerator<?>> of(Class<?> klass) {
        Class<?> generatorClass = keyToGenerator.get(klass);

        if (generatorClass != null) {
            try {
                return Optional.of((PrimaryKeyGenerator<?>) generatorClass.newInstance());
            } catch (Exception ignored) { }
        }

        return Optional.absent();
    }
}
