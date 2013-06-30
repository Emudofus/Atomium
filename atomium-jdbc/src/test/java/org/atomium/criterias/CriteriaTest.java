package org.atomium.criterias;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.atomium.criterias.Criterias.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Blackrush
 */
@RunWith(JUnit4.class)
public class CriteriaTest {
    private SqlCriteriaPrinter printer;

    @Before
    public void setUp() throws Exception {
        printer = new SqlCriteriaPrinter();
    }

    @Test
    public void testPrintSQL() throws Exception {
        // :test == :lel && :test >= :lel2 || !:test
        CriteriaInterface p1 = placeholder("test"), p2 = placeholder("lel"), p3 = placeholder("lel2");
        CriteriaInterface criteria = and(equal(p1, p2), or(higherOrEquals(p1, p3), not(p1)));

        String representation = printer.print(criteria);
        assertThat(representation, is(":test=:lel AND :test >= :lel2 OR !:test"));

        assertThat(
                printer.print(and(equal(placeholder("test"), value(1)), like(placeholder("helloWorld"), value("AHAHAH LELEL")))),
                is(":test=1 AND :helloWorld LIKE \'AHAHAH LELEL\'")
        );
    }
}
