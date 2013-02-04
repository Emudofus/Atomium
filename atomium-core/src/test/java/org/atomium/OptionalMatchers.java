package org.atomium;

import com.google.common.base.Optional;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * @author blackrush
 */
public final class OptionalMatchers {
    private static final class IsPresentMatcher<T> extends BaseMatcher<T> {
        private static final IsPresentMatcher<Object> INSTANCE = new IsPresentMatcher<Object>();
        private IsPresentMatcher() {}

        @Override
        public boolean matches(Object o) {
            return o instanceof Optional<?> && ((Optional) o).isPresent();

        }

        @Override
        public void describeTo(Description description) {
            description.appendText("is present");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> isPresent() {
        return (Matcher<T>) IsPresentMatcher.INSTANCE;
    }
}
