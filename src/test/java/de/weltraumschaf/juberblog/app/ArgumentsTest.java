package de.weltraumschaf.juberblog.app;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link Arguments}.
 *
 * @author Sven Strittmatter
 */
public class ArgumentsTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    @Test
    public void throwExceptionIfconstructWithNull() {
        thrown.expect(NullPointerException.class);
        new Arguments(null);
    }

    @Test
    public void getFirstArgument() {
        assertThat(new Arguments(new String[] {}).getFirstArgument(), is(equalTo("")));
        assertThat(new Arguments(new String[] {"foo"}).getFirstArgument(), is(equalTo("foo")));
        assertThat(new Arguments(new String[] {"foo", "bar"}).getFirstArgument(), is(equalTo("foo")));
        assertThat(new Arguments(new String[] {"foo", "bar", "baz"}).getFirstArgument(), is(equalTo("foo")));
    }

    @Test
    public void getTailArguments() {
        assertThat(new Arguments(new String[] {}).getTailArguments(), is(equalTo(new String[] {})));
        assertThat(new Arguments(new String[] {"foo"}).getTailArguments(), is(equalTo(new String[] {})));
        assertThat(new Arguments(new String[] {"foo", "bar"}).getTailArguments(),
                is(equalTo(new String[] {"bar"})));
        assertThat(new Arguments(new String[] {"foo", "bar", "baz"}).getTailArguments(),
                is(equalTo(new String[] {"bar", "baz"})));
    }

    @Test
    public void size() {
        assertThat(new Arguments(new String[] {}).size(), is(equalTo(0)));
        assertThat(new Arguments(new String[] {"foo"}).size(), is(equalTo(1)));
        assertThat(new Arguments(new String[] {"foo", "bar"}).size(), is(equalTo(2)));
        assertThat(new Arguments(new String[] {"foo", "bar", "baz"}).size(), is(equalTo(3)));
    }

    @Test
    public void isEmpty() {
        assertThat(new Arguments(new String[] {}).isEmpty(), is(equalTo(true)));
        assertThat(new Arguments(new String[] {"foo"}).isEmpty(), is(equalTo(false)));
    }

    @Test
    public void testToString() {
        assertThat(new Arguments(new String[] {"foo", "bar", "baz"}).toString(),
                is(equalTo("first: foo, tail: [bar, baz]")));
    }

}
