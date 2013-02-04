package org.atomium.pkey;

/**
 * @author blackrush
 */
public class IntKey extends NumberKey {
    public static IntKey create() {
        return new IntKey();
    }

    public static IntKey of(Integer integer) {
        return new IntKey(integer);
    }

    private final Integer key;

    protected IntKey(Integer key) {
        this.key = key;
    }

    protected IntKey() {
        this(0);
    }

    @Override
    public Integer toNumber() {
        return key;
    }
}
