package org.atomium;

/**
 * @author Blackrush
 */
public interface QueryInterface {
    NamedValues getBoundValues();
    Object getBoundValue(String key);
}
