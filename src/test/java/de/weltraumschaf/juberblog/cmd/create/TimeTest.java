package de.weltraumschaf.juberblog.cmd.create;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link Time}.
 *
 * @author Sven Strittmatter
 */
public class TimeTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    @Test
    public void invokeConstructorByReflectionThrowsException() throws Exception {
        assertThat(Time.class.getDeclaredConstructors().length, is(1));

        final Constructor<Time> ctor = Time.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        thrown.expect(either(instanceOf(UnsupportedOperationException.class))
                .or(instanceOf(InvocationTargetException.class)));
        ctor.newInstance();
    }

    @Test
    public void newProvider_neverNull() {}

    @Test
    public void newProvider_alwaysNewInstance() {}

    @Test
    public void newProvider_isOfTypeDefaultTimeProvider() {
        assertThat(Time.newProvider() instanceof DefaultTimeProvider, is(true));
    }
}
