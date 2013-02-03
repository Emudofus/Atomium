package org.atomium.pkey;

/**
 * @author blackrush
 */
public class LongKeyGenerator extends AbstractPrimaryKeyGenerator<LongKey> {
    private long key;

    @Override
    public void init(LongKey initial) {
        key = initial.toNumber().longValue();
    }

    @Override
    public LongKey next() {
        return LongKey.of(++key);
    }
}
