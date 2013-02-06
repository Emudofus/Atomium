package org.atomium;

import org.junit.Test;

import java.util.Iterator;

import static org.atomium.NamedValues.combine;
import static org.atomium.NamedValues.simple;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author blackrush
 */
public class NamedValuesTest {
    @Test
    public void combineTest() {
        NamedValues values = combine(simple().set("test", 1), simple().set("test", 2), simple().set("test", 3));

        Iterator<Object> it = values.iterator();
        assertThat(it.hasNext(), equalTo(true));
        assertThat(it.next(), equalTo((Object) 1));
        assertThat(it.hasNext(), equalTo(true));
        assertThat(it.next(), equalTo((Object) 2));
        assertThat(it.hasNext(), equalTo(true));
        assertThat(it.next(), equalTo((Object) 3));
        assertThat(it.hasNext(), equalTo(false));

        int i = 0;
        for (Object o : values) {
            assertThat(o, equalTo((Object) (++i)));
        }
    }
}
