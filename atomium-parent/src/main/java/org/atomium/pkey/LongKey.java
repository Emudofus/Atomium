package org.atomium.pkey;

/**
 * @author blackrush
 */
public class LongKey extends NumberKey {
    public static LongKey create() {
        return new LongKey();
    }

    public static LongKey of(Long integer) {
        return new LongKey(integer);
    }

    private final Long key;

    protected LongKey(Long key) {
        this.key = key;
    }

    protected LongKey() {
        this(0L);
    }

    @Override
    public Number toNumber() {
        return key;
    }
}
