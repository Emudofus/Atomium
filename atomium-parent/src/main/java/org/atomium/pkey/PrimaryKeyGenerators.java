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

    @SuppressWarnings("unchecked")
    public static <T extends PrimaryKey> Optional<PrimaryKeyGenerator<T>> of(Class<T> klass) {
        Class<?> generatorClass = keyToGenerator.get(klass);

        if (generatorClass != null) {
            try {
                return Optional.of((PrimaryKeyGenerator<T>) generatorClass.newInstance());
            } catch (Exception ignored) { }
        }

        return Optional.absent();
    }
}
