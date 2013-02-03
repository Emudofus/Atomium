package org.atomium.pkey;

/**
 * @author blackrush
 */
public class IntKeyGenerator extends AbstractPrimaryKeyGenerator<IntKey> {
    private int key;

    @Override
    public void init(IntKey initial) {
        key = initial.toNumber().intValue();
    }

    @Override
    public IntKey next() {
        return IntKey.of(++key);
    }
}
